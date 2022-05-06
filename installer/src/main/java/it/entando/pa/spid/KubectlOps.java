package it.entando.pa.spid;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class KubectlOps {

  private static final Logger logger = LogManager.getLogger(KubectlOps.class);

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

  public static boolean checkFileExists(String namespace, String podName, String src, String dst) {
    boolean fileExists = false;

    try(KubernetesClient client = new DefaultKubernetesClient()) {
      File dstFile = new File(dst);
      Path dstPath = dstFile.toPath();
      client.pods()
        .inNamespace(namespace)
        .withName(podName)
        .file(src)
        .copy(dstPath);
//      logger.debug("Exists: {} length: {}", dstFile.exists(), (dstFile.length() > 0));
      fileExists = dstFile.exists() && dstFile.length() > 0;
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
}
