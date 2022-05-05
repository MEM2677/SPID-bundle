package it.entando.pa.spid;

import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class Installer {

  private static final Logger logger = LogManager.getLogger(Installer.class);

  public static void main(String[] args) {
    System.out.println("\n");
    try {
      logger.info("SPID provider installer started");
      File installer = new File(PROVIDER_PATH);
      if (installer.exists()) {
        logger.debug("Provider file found: " + PROVIDER_PATH);
        // get pods for the given namespace
        V1PodList list = KubectlOps.listPodsNamespace(DEFAULT_NAMESPACE);
        if (null != list) {
          for (V1Pod item : list.getItems()) {
            String podName = item.getMetadata().getName();
            logger.debug("detected pod: " + podName);
            if (podName.contains(KEYCLOACK_POD_NAME)) {
              // TODO copy the provider to the Keycloak POD
              logger.info("copying provider to " + podName);
              // TODO config keycloak
            }
          }
        } else {
          System.out.println("No namespaces");
        }
      } else {
        logger.error("Provider not found! Rebuild the image or fix filename");
      }
    } catch (Throwable t) {
      logger.error("Installer error detected", t);
    }
    while (logger != null);
    logger.info("SPID provider installer completed execution");
  }

  public final static String PROVIDER_PATH = "/spid-provider/spid-provider.jar";
  public final static String PROVIDER_PATH_DEST_DIR = "/opt/jboss/keycloak/standalone/deployments";
  public final static String KEYCLOACK_POD_NAME = "default-sso-in-namespace-deployment";
  public final static String DEFAULT_NAMESPACE = "entando";
}
