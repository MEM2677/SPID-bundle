package it.entando.pa.spid;

public interface Constants {
  public final static String CERT_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/ca.crt";
  public final static String TOKEN_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/token";
  public final static String NAMESPACE_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/namespace";
  public final static String PROVIDER_FILENAME = "spid-provider.jar";
  public final static String PROVIDER_FILE_PATH = "/spid-provider/" + PROVIDER_FILENAME;
  public final static String PROVIDER_FILE_KC_PATH = "/opt/jboss/keycloak/standalone/deployments/" + PROVIDER_FILENAME;
  public final static String KEYCLOACK_POD_NAME_SIGNATURE = "default-sso-in-namespace-deployment";
  public final static String DEFAULT_NAMESPACE = "entando";
  public final static String KEYCLOAK_SECRET_NAME = "default-sso-in-namespace-admin-secret";
  public final static String INSTANCE_INGRESS_NAME = "default-sso-in-namespace-ingress";
}
