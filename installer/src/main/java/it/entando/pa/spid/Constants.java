package it.entando.pa.spid;

public interface Constants {

  boolean DEBUG_ENABLED = false;

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

  // keycloack related values
  String KEYCLOAK_CLIENT_ID = "admin-cli";
  String KEYCLOAK_CLIENT_SECRET = "admin-cli";
  String KEYCLOAK_DEFAULT_REALM = "entando";
  String KEYCLOAK_DEFAULT_AUTH_FLOW = "first broker login";
  String KEYCLOAK_NEW_AUTH_FLOW_NAME = "SPID first broker login";
}
