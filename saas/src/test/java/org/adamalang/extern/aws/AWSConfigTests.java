/*
 * This file is subject to the terms and conditions outlined in the
 * file 'LICENSE' (it's dual licensed) located in the root directory
 * near the README.md which you should also read. For more information
 * about the project which owns this file, see https://www.adama-platform.com/ .
 *
 * (c) 2021 - 2023 by Adama Platform Initiative, LLC
 */
package org.adamalang.extern.aws;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.adamalang.common.ConfigObject;
import org.adamalang.common.Json;
import org.junit.Assert;
import org.junit.Test;

public class AWSConfigTests {
  @Test
  public void coverage() throws Exception {
    ObjectNode aws = Json.newJsonObject();
    aws.put("access-key", "key");
    aws.put("secret-key", "secret");
    aws.put("init-from-email", "x@x.com");
    aws.put("init-reply-email", "x@x.com");
    aws.put("bucket", "bucket");
    aws.put("region", "region");
    aws.put("archive", "archive");
    aws.put("queue", "queue");
    AWSConfig config = new AWSConfig(new ConfigObject(aws));
    Assert.assertEquals("secret", config.credential.secretKey);
    Assert.assertEquals("key", config.credential.accessKeyId);
  }
}
