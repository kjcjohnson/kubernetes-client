/**
 * Copyright (C) 2015 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fabric8.knative.test.crud;

import io.fabric8.knative.serving.v1beta1.*;
import io.fabric8.knative.client.KnativeClient;
import io.fabric8.knative.mock.KnativeServer;

import org.junit.Rule;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ServiceCrudTest {
  
  @Rule
  public KnativeServer server = new KnativeServer(true, true);

  @Test
  public void shouldReturnEmptyList() {
    KnativeClient client = server.getKnativeClient();
    ServiceList serviceList = client.services().inNamespace("ns1").list();
    assertNotNull(serviceList);
    assertTrue(serviceList.getItems().isEmpty());
  }

  @Test
  public void shouldListAndGetService() {
    KnativeClient client = server.getKnativeClient();
    Service service2 = new ServiceBuilder().withNewMetadata().withName("service2").endMetadata().build();

    client.services().inNamespace("ns2").create(service2);
    ServiceList serviceList = client.services().inNamespace("ns2").list();
    assertNotNull(serviceList);
    assertEquals(1, serviceList.getItems().size());
    Service service = client.services().inNamespace("ns2").withName("service2").get();
    assertNotNull(service);
    assertEquals("service2", service.getMetadata().getName());
  }

  @Test
  public void shouldDeleteAService() {
    KnativeClient client = server.getKnativeClient();
    Service service3 = new ServiceBuilder().withNewMetadata().withName("service3").endMetadata().build();

    client.services().inNamespace("ns3").create(service3);
    Boolean deleted = client.services().inNamespace("ns3").withName("service3").delete();
    assertTrue(deleted);
  }
}
