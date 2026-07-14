package io.github.thirumalx.dto;

/**
 * @author Thirumal
 *         EsignDto represents the data transfer object for eSign requests.
 *         This DTO is used to transfer data between the client and the server.
 * 
 * @param applicationId       - Application Id (request origin application id)
 * @param signId              - Sign Id (eSign Request reference id)
 * @param firstName           - First Name (signatory first name)
 * @param middleName          - Middle Name (signatory middle name)
 * @param lastName            - Last Name (signatory last name)
 * @param dob                 - Date of Birth (signatory date of birth)
 * @param location            - Location (signatory location)
 * @param fileSourcePath      - File Source Path (location of the file to be
 *                            signed)
 * @param fileDestinationPath - File Destination Path (location to save the
 *                            signed file)
 * @param providerCode        - Provider Code (Optional provider to use for
 *                            eSign)
 * @param successUrl          - Success Url (url to redirect on success)
 * @param failureUrl          - Failure Url (url to redirect on failure)
 */
public record EsignDto(String applicationId, String signId,
        String firstName, String middleName, String lastName, String dob,
        String location,
        String fileSourcePath, String fileDestinationPath,
        String providerCode,
        String successUrl, String failureUrl) {

}
