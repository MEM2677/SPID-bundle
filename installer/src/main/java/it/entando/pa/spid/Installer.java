package it.entando.pa.spid;

import io.fabric8.kubernetes.api.model.Pod;
import it.entando.pa.spid.model.Credentials;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class Installer {

  private static final Logger logger = LogManager.getLogger(Installer.class);

  public static void main(String[] args) {
    try {
      logger.info("SPID provider installer started");
      File installer = new File(PROVIDER_FILE_PATH);
      if (installer.exists()) {
        logger.debug("Provider file found: " + PROVIDER_FILE_PATH);
        String namespace = getDefaultNamespace();
        // get pods for the given namespace
        List<Pod> list = ClusterOps.getPodsInNamespace(namespace);
        if (null != list) {
          for (Pod item : list) {
            String podName = item.getMetadata().getName();
            logger.debug("detected pod: " + podName);
            // check for KC POD
            if (podName.contains(KEYCLOACK_POD_NAME)) {
              logger.info("analysing pod " + podName);
              // copy the provider to the Keycloak POD
              if (!ClusterOps.checkFileExists(namespace, podName, PROVIDER_FILE_KC_PATH, "/tmp/file.txt")) {
                logger.info("installing {} in pod {}", PROVIDER_FILENAME, podName);
                ClusterOps.copyFile(namespace, podName, PROVIDER_FILE_PATH, PROVIDER_FILE_KC_PATH);
              } else {
                logger.info("Provider already installed in pod {}", podName);
              }
              // obtain keycloack admin username and password
              Credentials credentials = ClusterOps.getUsernameAndPassword(namespace, KEYCLOAK_SECRET_NAME);
              if (credentials != null) {
//                logger.info("Secret username: " + credentials.getUsername());
//                logger.info("Secret password: " + credentials.getPassword());
                logger.debug("Keycloak secrets for pod {} acquired", podName);
              }
              // get ingress for Keycloak
              String host = ClusterOps.getIngressHost(namespace, INSTANCE_INGRESS_NAME);
              logger.debug("keycloak host is {}", host);
            }
          }
        } else {
          logger.debug("No pod detected");
        }
      } else {
        logger.error("Provider not found! Rebuild the image or fix the filename");
      }
    } catch (Throwable t) {
      logger.error("Installer error detected", t);
    }

//    while (logger != null);
    logger.info("SPID provider installer completed execution");
  }

  private static String getDefaultNamespace() {
    String ns = "";
    try {
      File file = new File(NAMESPACE_FILE);
      if  (file.exists()) {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        if ((ns = br.readLine()) != null) {
          logger.debug("Default namespace is {}", ns);
          ns = ns.trim(); // paranoid, I know
        }
      } else {
        logger.warn(NAMESPACE_FILE + " does not exist");
      }
    } catch (Throwable t) {
      logger.error("error getting the default namespace", t);
    }
    return ns;
  }

  public final static String CERT_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/ca.crt";
  public final static String TOKEN_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/token";
  public final static String NAMESPACE_FILE = "/var/run/secrets/kubernetes.io/serviceaccount/namespace";

  public final static String PROVIDER_FILENAME = "spid-provider.jar";
  public final static String PROVIDER_FILE_PATH = "/spid-provider/" + PROVIDER_FILENAME;
  public final static String PROVIDER_FILE_KC_PATH = "/opt/jboss/keycloak/standalone/deployments/" + PROVIDER_FILENAME;
  public final static String PROVIDER_FILE_KC_TEST_PATH = "/opt/jboss/keycloak/standalone/deployments/README.txt";
  public final static String KEYCLOACK_POD_NAME = "default-sso-in-namespace-deployment";
  public final static String DEFAULT_NAMESPACE = "entando";

  public final static String KEYCLOAK_SECRET_NAME = "default-sso-in-namespace-admin-secret";
  public final static String INSTANCE_INGRESS_NAME = "default-sso-in-namespace-ingress";
}
