/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
 */
package org.adamalang.extern;

import org.adamalang.grpc.client.Client;
import org.adamalang.mysql.DataBase;

public class ExternNexus {

  public final Email email;
  public final DataBase dataBaseManagement;
  public final DataBase dataBaseDeployments;
  public final DataBase dataBaseBackend;
  public final Client client;

  public ExternNexus(Email email, DataBase dataBaseManagement, DataBase dataBaseDeployments,  DataBase dataBaseBackend, Client client) {
    this.email = email;
    this.dataBaseManagement = dataBaseManagement;
    this.dataBaseDeployments = dataBaseDeployments;
    this.dataBaseBackend = dataBaseBackend;
    this.client = client;
  }

  public void close() throws Exception {
    dataBaseDeployments.close();
    dataBaseManagement.close();
    client.shutdown();
  }
}
