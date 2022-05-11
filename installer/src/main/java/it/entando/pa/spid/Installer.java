package it.entando.pa.spid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.Pod;
import it.entando.pa.spid.dto.ConnectionInfo;
import it.entando.pa.spid.model.keycloak.AuthenticationFlow;
import it.entando.pa.spid.model.keycloak.Execution;
import it.entando.pa.spid.model.keycloak.IdentityProvider;
import it.entando.pa.spid.model.keycloak.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static it.entando.pa.spid.Constants.*;

public class Installer {

  private static final Logger logger = LogManager.getLogger(Installer.class);

  protected static void test1() {
    final String HOST = "spid-entando.192-168-49-2.nip.io";

    ConnectionInfo kcc = new ConnectionInfo(HOST);
    kcc.setUsername("entando_keycloak_admin");
    kcc.setPassword("51363ab6d2d54df5");
    Token token = RestApiOps.getAdminAccessToken(kcc.getHost(), kcc.getUsername(), kcc.getPassword(), KEYCLOAK_CLIENT_ID, KEYCLOAK_CLIENT_SECRET);
    System.out.println(">>> " + token.getAccessToken());
/*
    System.out.println("\n---\n---\n---");
    System.out.println("duplication: " + RestApiOps.duplicateAuthFlow(HOST, token));
    System.out.println("\n---\n---\n---");
    System.out.println("add execution: " + RestApiOps.addExecutable(HOST, token));
    System.out.println("\n---\n---\n---");
    Execution[] executions = RestApiOps.getExecutions(HOST, token);
    if (executions == null || executions.length == 0
      || !executions[executions.length - 1].getDisplayName().equals(KEYCLOAK_EXECUTION_EXPECTED_DISPLAY_NAME)) {
      logger.error("could not obtain the execution for the flow, aborting configuration");
      return;
    }
    String id = executions[executions.length - 1].getId();
    logger.info("Target execution ID is: " + id);
    System.out.println("\n---\n---\n---");
    for (int i = 0; i < 2; i++) {
      if (!RestApiOps.raiseExecutionPriority(HOST, token, id)) {
        logger.error("Could not raise the execution level of the target execution " + id);
        break;
      }
    }

    // 5A - REQUIRED for Automatically "Set Existing User"
    if (!updateExecutionRequirement(HOST, token, executions, KEYCLOAK_EXECUTION_EXPECTED_DISPLAY_NAME, "REQUIRED")) {
      logger.error("Cannot find target execution " + KEYCLOAK_EXECUTION_EXPECTED_DISPLAY_NAME + ", aborting setup" );
      return;
    }
    // 5B - DISABLED for "Confirm Link Existing Account"
    if (!updateExecutionRequirement(HOST, token, executions, KEYCLOAK_EXECUTION_CONFIRM_LINK_DISPLAY_NAME, "DISABLED")) {
      logger.error("Cannot find target execution " + KEYCLOAK_EXECUTION_CONFIRM_LINK_DISPLAY_NAME + ", aborting setup" );
      return;
    }
    // 5C - DISABLED for "SPID first broker login Account verification options"
    if (!updateExecutionRequirement(HOST, token, executions, KEYCLOAK_EXECUTION_VERIFICATION_OPTIONS_DISPLAY_NAME, "DISABLED")) {
      logger.error("Cannot find target execution " + KEYCLOAK_EXECUTION_VERIFICATION_OPTIONS_DISPLAY_NAME + ", aborting setup" );
      return;
    }
*/

    ObjectMapper objectMapper = new ObjectMapper();
    try {
      IdentityProvider idp = objectMapper.readValue(TEST_LOCAL_IdP, IdentityProvider.class);
      if (!RestApiOps.createIdentityProvider(HOST, token, idp)) {
        logger.error("Cannot configure the service provider [" + KEYCLOAK_IDP_DISPLAY_NAME + "], aborting setup" );
        return;
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  private static boolean updateExecutionRequirement(String host, Token token, Execution[] executions, String executionName, String requirement) {
    AuthenticationFlow updated = null;
    Optional<Execution> execOpt = findExecution(executions, executionName);

    if (execOpt.isPresent()) {
      Execution execution = execOpt.get();
      execution.setRequirement(requirement); // TODO constant
      updated = RestApiOps.updateExecution(host, token, execution);
    } else {
      logger.error("Cannot find target execution " + executionName + ", aborting setup" );
    }
    return updated != null;
  }

  private static void configureKeycloak(String host, Token token) {
    Execution[] executions;
    try {

      // 1 - configure authentication flow
      if (!RestApiOps.duplicateAuthFlow(host, token)) {
        logger.error("could not create authentication flow, aborting configuration");
        return;
      }
      logger.info("Keycloak config: authorization flow created with name [{}]", KEYCLOAK_NEW_AUTH_FLOW_NAME);
      // 2 - add executable
      if (!RestApiOps.addExecutable(host, token)) {
        logger.error("could not add execution to the authentication flow, aborting configuration");
        return;
      }
      logger.info("Added the new executor to the authentication flow");
      // 3 - get the id of the newly created execution
      executions = RestApiOps.getExecutions(host, token);
      if (executions == null || executions.length == 0
        || !executions[executions.length - 1].getDisplayName().equals(KEYCLOAK_EXECUTION_EXPECTED_DISPLAY_NAME)) {
        logger.error("could not obtain the execution for the flow, aborting configuration");
        return;
      }
      String id = executions[executions.length - 1].getId();
      logger.info("Target execution ID is: " + id);
      //  4 - move executable to its position
      for (int i = 0; i < 2; i++) {
        if (!RestApiOps.raiseExecutionPriority(host, token, id)) {
          logger.error("Could not raise the execution level of the target execution " + id);
          break;
        }
      }
      // 5 - edit requirements of the given executables

      // 5A - REQUIRED for Automatically "Set Existing User"
      if (!updateExecutionRequirement(host, token, executions, KEYCLOAK_EXECUTION_EXPECTED_DISPLAY_NAME, "REQUIRED")) {
        logger.error("Cannot find target execution [" + KEYCLOAK_EXECUTION_EXPECTED_DISPLAY_NAME + "], aborting setup" );
        return;
      }
      // 5B - DISABLED for "Confirm Link Existing Account"
      if (!updateExecutionRequirement(host, token, executions, KEYCLOAK_EXECUTION_CONFIRM_LINK_DISPLAY_NAME, "DISABLED")) {
        logger.error("Cannot find target execution [" + KEYCLOAK_EXECUTION_CONFIRM_LINK_DISPLAY_NAME + "], aborting setup" );
        return;
      }
      // 5C - DISABLED for "SPID first broker login Account verification options"
      if (!updateExecutionRequirement(host, token, executions, KEYCLOAK_EXECUTION_VERIFICATION_OPTIONS_DISPLAY_NAME, "DISABLED")) {
        logger.error("Cannot find target execution [" + KEYCLOAK_EXECUTION_VERIFICATION_OPTIONS_DISPLAY_NAME + "], aborting setup" );
        return;
      }
      // 6 - configure identity provider TODO customize data!
      ObjectMapper objectMapper = new ObjectMapper();
      IdentityProvider idp = objectMapper.readValue(TEST_LOCAL_IdP, IdentityProvider.class);
      if (!RestApiOps.createIdentityProvider(host, token, idp)) {
        logger.error("Cannot configure the service provider [" + KEYCLOAK_IDP_DISPLAY_NAME + "], aborting setup" );
        return;
      }
    } catch (Throwable t) {
      logger.error("unexpected error in configureKeycloak", t);
    }
  }

  protected static Optional<Execution> findExecution(Execution[] executions, String displayName) {
    return Arrays.asList(executions)
      .stream()
//      .peek(e -> System.out.println(">?> " + e.getDisplayName()))
      .filter(e -> e.getDisplayName().equals(displayName))
      .findFirst();
  }

  /**
   * Configure the keycloak pod first copying the provider JAR
   * @param namespace
   * @param pod
   */
  private static void configurePod(String namespace, Pod pod) {
    String podName = pod.getMetadata().getName();
    // check for KC POD
    if (isKeycloakPod(podName)) {
      logger.info("analysing pod " + podName);
      // copy the provider to the Keycloak POD
      if (!ClusterOps.checkFileExists(namespace, podName, PROVIDER_FILE_DESTINATION_PATH)) {
        logger.info("installing {} in pod {}", PROVIDER_FILENAME, podName);
        ClusterOps.copyFile(namespace, podName, PROVIDER_FILE_LOCAL_PATH, PROVIDER_FILE_DESTINATION_PATH);
        logger.info("waiting for provider setup...");
        // wait for installation completed
        do {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            logger.error("error in configurePod", e);
          }
        } while (!ClusterOps.checkFileExists(namespace, podName, DEPLOYED_PROVIDER_FILE_DESTINATION_PATH));
        logger.info("...installation of the provider completed");
      } else {
        logger.info("Provider already installed in pod {}", podName);
      }
      // get ingress for Keycloak
      String host = ClusterOps.getIngressHost(namespace, INSTANCE_INGRESS_NAME);
      logger.debug("keycloak host is {}", host);
      // obtain keycloak admin username and password by reading the secret
      ConnectionInfo info = new ConnectionInfo(host);
      ClusterOps.readSecret(info, namespace, KEYCLOAK_SECRET_NAME);
      // get the admin access token
      Token token = RestApiOps.getAdminAccessToken(info.getHost(), info.getUsername(), info.getPassword(),
        KEYCLOAK_CLIENT_ID, KEYCLOAK_CLIENT_SECRET);
//      if (token != null) {
//        logger.info("HAVE TOKEN");
//        logger.info("ACCESS TOKEN " + token.getAccessToken());
//      }
      // configure keycloak itself
      configureKeycloak(host, token);
    }
  }

  private static boolean isKeycloakPod(String podName) {
    return podName.contains(KEYCLOACK_POD_NAME_SIGNATURE);
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

  public static void main(String[] args) {
    try {
      logger.info("SPID provider installer started");
      File installer = new File(PROVIDER_FILE_LOCAL_PATH);
      if (installer.exists()) {
        logger.debug("Provider file found: " + PROVIDER_FILE_LOCAL_PATH);
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
      test1();
    } catch (Throwable t) {
      logger.error("Installer error detected", t);
    }
//    while (logger != null);
    logger.info("SPID provider installer completed execution");
  }


}
