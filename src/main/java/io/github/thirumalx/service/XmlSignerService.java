package io.github.thirumalx.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import jakarta.annotation.PostConstruct;
import javax.xml.crypto.dsig.*;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Collections;
/**
 * @author Thirumal
 * Service for signing XML documents using a configured keystore.
 * This service is used by the EmudraEsignProvider to sign the eSign request XML.
 */
@Service
public class XmlSignerService {

    private static final Logger logger = LoggerFactory.getLogger(XmlSignerService.class);

    @Value("${esign.emudhra.keystore.path}")
    private Resource keystoreResource;

    @Value("${esign.emudhra.keystore.password}")
    private String keystorePassword;

    @Value("${esign.emudhra.keystore.alias}")
    private String keystoreAlias;

    @Value("${esign.emudhra.keystore.type}")
    private String keystoreType;

    private PrivateKey privateKey;
    private X509Certificate certificate;

    @PostConstruct
    public void init() {
        try {
            logger.info("Initializing XML Signer Service with Keystore type: {}", keystoreType);
            KeyStore keyStore = KeyStore.getInstance(keystoreType);
            
            if (keystoreResource != null && keystoreResource.exists()) {
                keyStore.load(keystoreResource.getInputStream(), keystorePassword.toCharArray());
                privateKey = (PrivateKey) keyStore.getKey(keystoreAlias, keystorePassword.toCharArray());
                certificate = (X509Certificate) keyStore.getCertificate(keystoreAlias);
                logger.info("Successfully loaded Private Key and Certificate for alias: {}", keystoreAlias);
            } else {
                logger.warn("Keystore resource not found at {}. XML Signing will fail until configured.", keystoreResource);
            }
        } catch (Exception e) {
            logger.error("Failed to initialize XML Signer Service", e);
        }
    }

    public String signXml(String xmlString) throws Exception {
        logger.debug("Signing XML document");
        if (privateKey == null || certificate == null) {
            throw new IllegalStateException("Keystore is not properly configured. Cannot sign XML.");
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xmlString)));

        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");

        Reference ref = fac.newReference("",
                fac.newDigestMethod(DigestMethod.SHA1, null),
                Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)),
                null, null);

        SignedInfo si = fac.newSignedInfo(
                fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE, (C14NMethodParameterSpec) null),
                fac.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                Collections.singletonList(ref));

        KeyInfoFactory kif = fac.getKeyInfoFactory();
        X509Data xd = kif.newX509Data(Collections.singletonList(certificate));
        KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

        DOMSignContext dsc = new DOMSignContext(privateKey, doc.getDocumentElement());

        XMLSignature signature = fac.newXMLSignature(si, ki);
        signature.sign(dsc);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter sw = new StringWriter();
        sw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        trans.transform(new DOMSource(doc), new StreamResult(sw));

        return sw.toString();
    }
}
