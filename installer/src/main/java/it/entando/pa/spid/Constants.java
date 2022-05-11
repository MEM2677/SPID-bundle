package it.entando.pa.spid;

public interface Constants {

  boolean REST_API_DEBUG_ENABLED = true;

  // constant paths within a POD
//  String CERT_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/ca.crt";
//  String TOKEN_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/token";
  String NAMESPACE_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/namespace";

  // declarations related to the installer itself
  String PROVIDER_FILENAME = "spid-provider.jar";
  String PROVIDER_FILE_LOCAL_PATH = "/spid-provider/" + PROVIDER_FILENAME;
  String PROVIDER_FILE_DESTINATION_PATH = "/opt/jboss/keycloak/standalone/deployments/" + PROVIDER_FILENAME;
  String DEPLOYED_PROVIDER_FILE_DESTINATION_PATH = "/opt/jboss/keycloak/standalone/deployments/" + PROVIDER_FILENAME + ".deployed";

  // kubernetes resource names as created by Entando 7.x
  String KEYCLOACK_POD_NAME_SIGNATURE = "default-sso-in-namespace-deployment";
//  String DEFAULT_NAMESPACE = "entando";
  String KEYCLOAK_SECRET_NAME = "default-sso-in-namespace-admin-secret";
  String INSTANCE_INGRESS_NAME = "default-sso-in-namespace-ingress";

  // keycloak related values
  String KEYCLOAK_CLIENT_ID = "admin-cli";
  String KEYCLOAK_CLIENT_SECRET = "admin-cli";
  String KEYCLOAK_DEFAULT_REALM = "entando";
  String KEYCLOAK_DEFAULT_AUTH_FLOW = "first broker login";
  String KEYCLOAK_NEW_AUTH_FLOW_NAME = "SPID first broker login";
  String KEYCLOAK_EXECUTION_HANDLE_EXISTING_ACCOUNT_NAME = KEYCLOAK_NEW_AUTH_FLOW_NAME + " Handle Existing Account";
  String KEYCLOAK_EXECUTION_EXPECTED_DISPLAY_NAME = "Automatically set existing user";
  String KEYCLOAK_EXECUTION_CONFIRM_LINK_DISPLAY_NAME = "Confirm link existing account";
  String KEYCLOAK_EXECUTION_VERIFICATION_OPTIONS_DISPLAY_NAME = KEYCLOAK_NEW_AUTH_FLOW_NAME + " Account verification options";
  String KEYCLOAK_IDP_DISPLAY_NAME = "SPID TEST LOCAL";
  String KEYCLOAK_IDP_ALIAS = "spid-test-local";

  // Identity provider settings for local environment
  String TEST_LOCAL_IdP= "{\n" +
    "  \"alias\": \"" + KEYCLOAK_IDP_ALIAS + "\",\n" +
    "  \"displayName\": \"" + KEYCLOAK_IDP_DISPLAY_NAME + "\",\n" +
    "  \"providerId\": \"spid\",\n" +
    "  \"enabled\": true,\n" +
    "  \"updateProfileFirstLoginMode\": \"on\",\n" +
    "  \"trustEmail\": true,\n" +
    "  \"storeToken\": false,\n" +
    "  \"addReadTokenRoleOnCreate\": false,\n" +
    "  \"authenticateByDefault\": false,\n" +
    "  \"linkOnly\": false,\n" +
    "  \"firstBrokerLoginFlowAlias\": \"" + KEYCLOAK_NEW_AUTH_FLOW_NAME + "\",\n" +
    "  \"config\": {\n" +
    "    \"authnContextClassRefs\": \"[\\\"https://www.spid.gov.it/SpidL1\\\"]\",\n" +
    "    \"otherContactPhone\": \"+395556935632\",\n" +
    "    \"postBindingLogout\": \"true\",\n" +
    "    \"postBindingResponse\": \"true\",\n" +
    "    \"singleLogoutServiceUrl\": \"https://localhost:8443/demo/samlsso\",\n" +
    "    \"organizationDisplayNames\": \"en|Organization, it|Organizzazione\",\n" +
    "    \"debugEnabled\": \"true\",\n" +
    "    \"organizationUrls\": \"it|http://192-168-49-2.nip.io/entando-de-app, en|http://192-168-49-2.nip.io/auth\",\n" +
    "    \"xmlSigKeyInfoKeyNameTransformer\": \"NONE\",\n" +
    "    \"idpEntityId\": \"https://localhost:8443/demo\",\n" +
    "    \"loginHint\": \"false\",\n" +
    "    \"allowCreate\": \"true\",\n" +
    "    \"organizationNames\": \"en|Organization, it|Organizzazione\",\n" +
    "    \"authnContextComparisonType\": \"minimum\",\n" +
    "    \"syncMode\": \"FORCE\",\n" +
    "    \"singleSignOnServiceUrl\": \"https://localhost:8443/demo/samlsso\",\n" +
    "    \"wantAuthnRequestsSigned\": \"true\",\n" +
    "    \"validateSignature\": \"true\",\n" +
    "    \"signingCertificate\": \"MIIEGDCCAwCgAwIBAgIJAOrYj9oLEJCwMA0GCSqGSIb3DQEBCwUAMGUxCzAJBgNVBAYTAklUMQ4wDAYDVQQIEwVJdGFseTENMAsGA1UEBxMEUm9tZTENMAsGA1UEChMEQWdJRDESMBAGA1UECxMJQWdJRCBURVNUMRQwEgYDVQQDEwthZ2lkLmdvdi5pdDAeFw0xOTA0MTExMDAyMDhaFw0yNTAzMDgxMDAyMDhaMGUxCzAJBgNVBAYTAklUMQ4wDAYDVQQIEwVJdGFseTENMAsGA1UEBxMEUm9tZTENMAsGA1UEChMEQWdJRDESMBAGA1UECxMJQWdJRCBURVNUMRQwEgYDVQQDEwthZ2lkLmdvdi5pdDCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAK8kJVo+ugRrbbv9xhXCuVrqi4B7/MQzQc62ocwlFFujJNd4m1mXkUHFbgvwhRkQqo2DAmFeHiwCkJT3K1eeXIFhNFFroEzGPzONyekLpjNvmYIs1CFvirGOj0bkEiGaKEs+/umzGjxIhy5JQlqXE96y1+Izp2QhJimDK0/KNij8I1bzxseP0Ygc4SFveKS+7QO+PrLzWklEWGMs4DM5Zc3VRK7g4LWPWZhKdImC1rnS+/lEmHSvHisdVp/DJtbSrZwSYTRvTTz5IZDSq4kAzrDfpj16h7b3t3nFGc8UoY2Ro4tRZ3ahJ2r3b79yK6C5phY7CAANuW3gDdhVjiBNYs0CAwEAAaOByjCBxzAdBgNVHQ4EFgQU3/7kV2tbdFtphbSA4LH7+w8SkcwwgZcGA1UdIwSBjzCBjIAU3/7kV2tbdFtphbSA4LH7+w8SkcyhaaRnMGUxCzAJBgNVBAYTAklUMQ4wDAYDVQQIEwVJdGFseTENMAsGA1UEBxMEUm9tZTENMAsGA1UEChMEQWdJRDESMBAGA1UECxMJQWdJRCBURVNUMRQwEgYDVQQDEwthZ2lkLmdvdi5pdIIJAOrYj9oLEJCwMAwGA1UdEwQFMAMBAf8wDQYJKoZIhvcNAQELBQADggEBAJNFqXg/V3aimJKUmUaqmQEEoSc3qvXFITvT5f5bKw9yk/NVhR6wndL+z/24h1OdRqs76blgH8k116qWNkkDtt0AlSjQOx5qvFYh1UviOjNdRI4WkYONSw+vuavcx+fB6O5JDHNmMhMySKTnmRqTkyhjrch7zaFIWUSV7hsBuxpqmrWDoLWdXbV3eFH3mINA5AoIY/m0bZtzZ7YNgiFWzxQgekpxd0vcTseMnCcXnsAlctdir0FoCZztxMuZjlBjwLTtM6Ry3/48LMM8Z+lw7NMciKLLTGQyU8XmKKSSOh0dGh5Lrlt5GxIIJkH81C0YimWebz8464QPL3RbLnTKg+c=\",\n" +
    "    \"nameIDPolicyFormat\": \"urn:oasis:names:tc:SAML:2.0:nameid-format:transient\",\n" +
    "    \"principalAttribute\": \"fiscalNumber\",\n" +
    "    \"entityId\": \"http://192.168.1.49:9090/auth/realms/Entando\",\n" +
    "    \"otherContactCompany\": \"Entando Srl\",\n" +
    "    \"signatureAlgorithm\": \"RSA_SHA256\",\n" +
    "    \"wantAssertionsEncrypted\": \"false\",\n" +
    "    \"useJwksUrl\": \"true\",\n" +
    "    \"wantAssertionsSigned\": \"true\",\n" +
    "    \"otherContactIpaCode\": \"bastachesia@gmail.com\",\n" +
    "    \"postBindingAuthnRequest\": \"true\",\n" +
    "    \"forceAuthn\": \"false\",\n" +
    "    \"attributeConsumingServiceIndex\": \"1\",\n" +
    "    \"addExtensionsElementWithKeyInfo\": \"false\",\n" +
    "    \"principalType\": \"ATTRIBUTE\"}\n" +
    "  }";
}
