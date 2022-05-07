package it.entando.pa.spid;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.networking.v1beta1.Ingress;
import io.fabric8.kubernetes.api.model.networking.v1beta1.IngressRule;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import it.entando.pa.spid.dto.ConnectionInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class ClusterOps {

  private static final Logger logger = LogManager.getLogger(ClusterOps.class);

  public static List<Pod> getPodsInNamespace(String namespace) {
    try {
      KubernetesClient client = new DefaultKubernetesClient();
      List<Pod> pods = client.pods().inNamespace(namespace).list().getItems();
//      pods.forEach(p -> logger.info(">>>" + p.getMetadata().getName()));
      return pods;
    } catch (Throwable t) {
      logger.error("Errore in getPods() ", t.getMessage());
    }
    return null;
  }

  public static boolean checkFileExists(String namespace, String podName, String src) {
    boolean fileExists = false;

    try(KubernetesClient client = new DefaultKubernetesClient()) {
      File dstFile = File.createTempFile("tst_", ".tmp");
      Path dstPath = dstFile.toPath();
      client.pods()
        .inNamespace(namespace)
        .withName(podName)
        .file(src)
        .copy(dstPath);
//      logger.debug("Exists: {} length: {}", dstFile.exists(), (dstFile.length() > 0));
      fileExists = dstFile.length() > 0;
      dstFile.delete();
    } catch (Throwable t) {
      logger.error("Error in checkFileExists", t);
    }
    return fileExists;
  }

  public static void copyFile(String namespace, String podName, String src, String dst) {
    try(KubernetesClient client = new DefaultKubernetesClient()) {
      File uploadFile = new File(src);

      if (uploadFile.exists()) {
        client.pods()
          .inNamespace(namespace)
          .withName(podName)
          .file(dst)
          .upload(uploadFile.toPath());
      } else {
        logger.warn("src file {} to upload missing", src);
      }
    } catch (Throwable t) {
      logger.error("Error in copyFile", t);
    }
  }

  public static void readSecret(ConnectionInfo info, String namespace, String secretName) {
    ConnectionInfo connectionInfo = null;

    try(KubernetesClient client = new DefaultKubernetesClient()) {
      Secret secret = client.secrets()
        .inNamespace(namespace)
        .withName(secretName)
        .get();

      if (secret != null) {
//        logger.info("HAVE SECRET!");
//        secret.getData().keySet()
//          .forEach(k -> logger.info("key {}: {}", k, secret.getData().get(k)));
        String encPassword = secret.getData().get(KEY_SECRET_PASSWORD);
        String encUsername = secret.getData().get(KEY_SECRET_USERNAME);
        info.setLogin(encUsername, encPassword);
      }
    } catch (Throwable t) {
      logger.error("Error in getUsernameAndPassword", t);
    }
  }

  public static String getIngressHost(String namespace, String ingressname) {
    String host = null;
    try (KubernetesClient client = new DefaultKubernetesClient()) {
      Ingress ingress = client.network()
        .ingresses()
        .inNamespace(namespace)
        .withName(ingressname)
        .get();
        if (ingress != null) {
//          logger.info("HAVE INGRESS");
//          ingress.getSpec().getRules().forEach(r -> logger.info("{}", r.getHost()));
          if (ingress.getSpec() != null
            && ingress.getSpec().getRules() != null)  {
            IngressRule rule = ingress.getSpec().getRules().get(0);
            host = rule.getHost();
          } else {
            logger.warn("Have ingress but cannot get the 'host' setting");
          }
        }
    } catch (Throwable t) {
      logger.error("Error in getIngressHost", t);
    }
    return host;
  }

  public final static String KEY_SECRET_PASSWORD = "password";
  public final static String KEY_SECRET_USERNAME = "username";
}
