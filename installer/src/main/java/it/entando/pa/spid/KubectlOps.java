package it.entando.pa.spid;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;

import java.io.IOException;

public class KubectlOps {

  public static V1PodList listPodsNamespace(String namespace) throws IOException, ApiException {

    // loading the in-cluster config, including:
    //   1. service-account CA
    //   2. service-account bearer-token
    //   3. service-account namespace
    //   4. master endpoints(ip, port) from pre-set environment variables
    ApiClient client = ClientBuilder.cluster().build();

    // if you prefer not to refresh service account token, please use:
    // ApiClient client = ClientBuilder.oldCluster().build();

    // set the global default api-client to the in-cluster one from above
    Configuration.setDefaultApiClient(client);

    // the CoreV1Api loads default api-client from global configuration.
    CoreV1Api api = new CoreV1Api();

    // invokes the CoreV1Api client
    V1PodList list =
       api.listNamespacedPod(namespace, null, null, null, null, null, null, null, null, null, null);
    return list;
  }

}
