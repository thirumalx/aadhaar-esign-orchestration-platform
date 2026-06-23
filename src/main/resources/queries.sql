# Application Queries
Application.create=INSERT INTO public.application (application_name, application_code, webhook_url, update_info) VALUES (:application_name, :application_code, :webhook_url, :update_info)
Application.get=SELECT * FROM public.application WHERE application_id = :application_id
Application.list=SELECT * FROM public.application ORDER BY application_id DESC
Application.update=UPDATE public.application SET application_name = :application_name, application_code = :application_code, webhook_url = :webhook_url, update_info = :update_info, updated_at = current_timestamp WHERE application_id = :application_id
Application.delete=DELETE FROM public.application WHERE application_id = :application_id

# SignatureProvider Queries
SignatureProvider.create=INSERT INTO public.signature_provider (signature_provider_id, provider_code, provider_name, priority) VALUES (:signature_provider_id, :provider_code, :provider_name, :priority)
SignatureProvider.get=SELECT * FROM public.signature_provider WHERE signature_provider_id = :signature_provider_id
SignatureProvider.list=SELECT * FROM public.signature_provider ORDER BY signature_provider_id DESC
SignatureProvider.update=UPDATE public.signature_provider SET provider_code = :provider_code, provider_name = :provider_name, priority = :priority, updated_at = current_timestamp WHERE signature_provider_id = :signature_provider_id
SignatureProvider.delete=DELETE FROM public.signature_provider WHERE signature_provider_id = :signature_provider_id

# ProviderCapability Queries
ProviderCapability.create=INSERT INTO public.provider_capability (signature_provider_id, provider_cd) VALUES (:signature_provider_id, :provider_cd)
ProviderCapability.get=SELECT * FROM public.provider_capability WHERE provider_capability_id = :provider_capability_id
ProviderCapability.list=SELECT * FROM public.provider_capability ORDER BY provider_capability_id DESC
ProviderCapability.update=UPDATE public.provider_capability SET signature_provider_id = :signature_provider_id, provider_cd = :provider_cd, updated_at = current_timestamp WHERE provider_capability_id = :provider_capability_id
ProviderCapability.delete=DELETE FROM public.provider_capability WHERE provider_capability_id = :provider_capability_id

# ProviderConfiguration Queries
ProviderConfiguration.create=INSERT INTO public.provider_configuration (signature_provider_id, environment_cd, api_url, health_url, timeout_ms, retry_count, api_key, secret, certificate_reference, update_info) VALUES (:signature_provider_id, :environment_cd, :api_url, :health_url, :timeout_ms, :retry_count, :api_key, :secret, :certificate_reference, :update_info)
ProviderConfiguration.get=SELECT * FROM public.provider_configuration WHERE provider_configuration_id = :provider_configuration_id
ProviderConfiguration.list=SELECT * FROM public.provider_configuration ORDER BY provider_configuration_id DESC
ProviderConfiguration.update=UPDATE public.provider_configuration SET signature_provider_id = :signature_provider_id, environment_cd = :environment_cd, api_url = :api_url, health_url = :health_url, timeout_ms = :timeout_ms, retry_count = :retry_count, api_key = :api_key, secret = :secret, certificate_reference = :certificate_reference, update_info = :update_info, updated_at = current_timestamp WHERE provider_configuration_id = :provider_configuration_id
ProviderConfiguration.delete=DELETE FROM public.provider_configuration WHERE provider_configuration_id = :provider_configuration_id

# SignatureRequest Queries
SignatureRequest.create=INSERT INTO public.signature_request (application_id, status_cd, reference_no, original_document_hash, original_document_path, request_payload, update_info) VALUES (:application_id, :status_cd, :reference_no, :original_document_hash, :original_document_path, :request_payload::jsonb, :update_info)
SignatureRequest.get=SELECT * FROM public.signature_request WHERE signature_request_id = :signature_request_id
SignatureRequest.list=SELECT * FROM public.signature_request ORDER BY signature_request_id DESC
SignatureRequest.update=UPDATE public.signature_request SET application_id = :application_id, status_cd = :status_cd, reference_no = :reference_no, original_document_hash = :original_document_hash, original_document_path = :original_document_path, request_payload = :request_payload::jsonb, update_info = :update_info, updated_at = current_timestamp WHERE signature_request_id = :signature_request_id
SignatureRequest.delete=DELETE FROM public.signature_request WHERE signature_request_id = :signature_request_id

# SignedDocument Queries
SignedDocument.create=INSERT INTO public.signed_document (signature_request_id, storage_path, signed_hash, signed_time, signer_name, signer_yob, signer_gender, signer_pin, signer_aadhaar_suffix) VALUES (:signature_request_id, :storage_path, :signed_hash, :signed_time, :signer_name, :signer_yob, :signer_gender, :signer_pin, :signer_aadhaar_suffix)
SignedDocument.get=SELECT * FROM public.signed_document WHERE signed_document_id = :signed_document_id
SignedDocument.list=SELECT * FROM public.signed_document ORDER BY signed_document_id DESC
SignedDocument.update=UPDATE public.signed_document SET signature_request_id = :signature_request_id, storage_path = :storage_path, signed_hash = :signed_hash, signed_time = :signed_time, signer_name = :signer_name, signer_yob = :signer_yob, signer_gender = :signer_gender, signer_pin = :signer_pin, signer_aadhaar_suffix = :signer_aadhaar_suffix, updated_at = current_timestamp WHERE signed_document_id = :signed_document_id
SignedDocument.delete=DELETE FROM public.signed_document WHERE signed_document_id = :signed_document_id

# SignatureAttempt Queries
SignatureAttempt.create=INSERT INTO public.signature_attempt (signature_request_id, provider_configuration_id, status_cd, request_sent_at, response_received_at, provider_transaction_id, request_payload, response_payload, http_status, provider_status_code, provider_status_message, error_code, error_message) VALUES (:signature_request_id, :provider_configuration_id, :status_cd, :request_sent_at, :response_received_at, :provider_transaction_id, :request_payload::jsonb, :response_payload::jsonb, :http_status, :provider_status_code, :provider_status_message, :error_code, :error_message)
SignatureAttempt.get=SELECT * FROM public.signature_attempt WHERE signature_attempt_id = :signature_attempt_id
SignatureAttempt.list=SELECT * FROM public.signature_attempt ORDER BY signature_attempt_id DESC
SignatureAttempt.update=UPDATE public.signature_attempt SET signature_request_id = :signature_request_id, provider_configuration_id = :provider_configuration_id, status_cd = :status_cd, request_sent_at = :request_sent_at, response_received_at = :response_received_at, provider_transaction_id = :provider_transaction_id, request_payload = :request_payload::jsonb, response_payload = :response_payload::jsonb, http_status = :http_status, provider_status_code = :provider_status_code, provider_status_message = :provider_status_message, error_code = :error_code, error_message = :error_message, updated_at = current_timestamp WHERE signature_attempt_id = :signature_attempt_id
SignatureAttempt.delete=DELETE FROM public.signature_attempt WHERE signature_attempt_id = :signature_attempt_id
