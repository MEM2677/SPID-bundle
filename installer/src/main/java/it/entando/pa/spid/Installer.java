package it.entando.pa.spid;

import io.fabric8.kubernetes.api.model.Pod;
import it.entando.pa.spid.model.Credentials;
import it.entando.pa.spid.model.KCCredentials;
import it.entando.pa.spid.model.KCToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import static it.entando.pa.spid.Constants.*;

public class Installer {

  private static final Logger logger = LogManager.getLogger(Installer.class);

//  protected static void test1() {
//    KCCredentials kcc = new KCCredentials();
//    kcc.setUsername("entando_keycloak_admin");
//    kcc.setPassword("74559569574c4c14");
//    kcc.setClientId("admin-cli");
//    kcc.setClientSecret("admin-cli");
//    KCToken token = RestApiOps.getAdminAccessToken("spid-entando.192-168-49-2.nip.io", kcc);
//    System.out.println(">>> " + token.getAccessToken());
//  }

  /**
   * Configure the pod
   *
   * @param namespace
   * @param pod
   */
  private static void configurePod(String namespace, Pod pod) {
    String podName = pod.getMetadata().getName();
    // check for KC POD
    if (isKeycloakPod(podName)) {
      logger.info("analysing pod " + podName);
      // copy the provider to the Keycloak POD
      if (!ClusterOps.checkFileExists(namespace, podName, PROVIDER_FILE_KC_PATH, "/tmp/file.txt")) {
        logger.info("installing {} in pod {}", PROVIDER_FILENAME, podName);
        ClusterOps.copyFile(namespace, podName, PROVIDER_FILE_PATH, PROVIDER_FILE_KC_PATH);
      } else {
        logger.info("Provider already installed in pod {}", podName);
      }
      // obtain keycloak admin username and password
      Credentials credentials = ClusterOps.getUsernameAndPassword(namespace, KEYCLOAK_SECRET_NAME);
      if (credentials != null) {
//                logger.info("Secret username: " + credentials.getUsername());
//                logger.info("Secret password: " + credentials.getPassword());
        logger.debug("Keycloak secrets for pod {} acquired", podName);
      }
      // get ingress for Keycloak
      String host = ClusterOps.getIngressHost(namespace, INSTANCE_INGRESS_NAME);
      logger.debug("keycloak host is {}", host);

      KCCredentials kcc = new KCCredentials();
      kcc.setUsername(credentials.getUsername());
      kcc.setPassword(credentials.getPassword());
      kcc.setClientId("admin-cli");
      kcc.setClientSecret("admin-cli");
      KCToken token = RestApiOps.getAdminAccessToken(host, kcc);
      if (token != null) {
        logger.info("HAVE TOKEN");
        logger.info("ACCESS TOKEN " + token.getAccessToken());
      }
    }
  }

  private static boolean isKeycloakPod(String podName) {
    return podName.contains(KEYCLOACK_POD_NAME_SIGNATURE);
  }

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
          for (Pod current : list) {
            configurePod(namespace, current);
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

}
