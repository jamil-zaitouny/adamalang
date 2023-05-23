package org.adamalang.cli.router;

import java.util.HashMap;
import java.util.Map;

public class CliElement {
  public String name;
  public String doc;
  public Map<String, CliElement> Commands = new HashMap<>();
  public Map<String, ArgumentItem> Arguments = new HashMap<>();
  public static Map<String, ArgumentItem> fullArgumentList = populateArgs();
  public static Map<String, CliElement> Groups = populateGroups();

  public CliElement(HashMap<String, CliElement> commands, String name, String type, String doc) {
    this.name = name;
    this.doc = doc;
    if (type.equals("group")) {
      Commands = commands;
    }
  }

  public CliElement(HashMap<String, ArgumentItem> arguments, String name, String doc) {
    this.doc = doc;
    this.name = name;
    Arguments = arguments;
  }
  public static HashMap<String, CliElement> populateGroups() {
    HashMap<String, CliElement> returnMap = new HashMap<>();

    HashMap<String, CliElement> spaceCommands = new HashMap<>();
    HashMap<String, ArgumentItem> createSpaceArgs = new HashMap<>();
    createSpaceArgs.put("--space", fullArgumentList.get("--space"));
    spaceCommands.put("create", new CliElement(createSpaceArgs, "create", "Create a space"));

    HashMap<String, ArgumentItem> deleteSpaceArgs = new HashMap<>();
    deleteSpaceArgs.put("--space", fullArgumentList.get("--space"));
    spaceCommands.put("delete", new CliElement(deleteSpaceArgs, "delete", "Placeholder"));

    HashMap<String, ArgumentItem> deploySpaceArgs = new HashMap<>();
    deploySpaceArgs.put("--space", fullArgumentList.get("--space"));
    deploySpaceArgs.put("--plan", fullArgumentList.get("--plan"));
    deploySpaceArgs.put("--file", fullArgumentList.get("--file"));
    spaceCommands.put("deploy", new CliElement(deploySpaceArgs, "deploy", "Placeholder"));

    HashMap<String, ArgumentItem> setRxhtmlSpaceArgs = new HashMap<>();
    setRxhtmlSpaceArgs.put("--space", fullArgumentList.get("--space"));
    setRxhtmlSpaceArgs.put("--file", fullArgumentList.get("--file"));
    spaceCommands.put("set-rxhtml", new CliElement(setRxhtmlSpaceArgs, "set-rxhtml", "Placeholder"));

    HashMap<String, ArgumentItem> uploadSpaceArgs = new HashMap<>();
    uploadSpaceArgs.put("--space", fullArgumentList.get("--space"));
    uploadSpaceArgs.put("--gc", fullArgumentList.get("--gc"));
    spaceCommands.put("upload", new CliElement(uploadSpaceArgs, "upload", "Placeholder"));

    HashMap<String, ArgumentItem> downloadSpaceArgs = new HashMap<>();
    downloadSpaceArgs.put("--space", fullArgumentList.get("--space"));
    spaceCommands.put("download", new CliElement(downloadSpaceArgs, "download", "Placeholder"));

    HashMap<String, ArgumentItem> listSpaceArgs = new HashMap<>();
    listSpaceArgs.put("--marker", fullArgumentList.get("--marker"));
    listSpaceArgs.put("--limit", fullArgumentList.get("--limit"));
    spaceCommands.put("list", new CliElement(listSpaceArgs, "list", "Placeholder"));

    HashMap<String, ArgumentItem> usageSpaceArgs = new HashMap<>();
    usageSpaceArgs.put("--space", fullArgumentList.get("--space"));
    usageSpaceArgs.put("--limit", fullArgumentList.get("--limit"));
    spaceCommands.put("usage", new CliElement(usageSpaceArgs, "usage", "Placeholder"));

    HashMap<String, ArgumentItem> reflectSpaceArgs = new HashMap<>();
    reflectSpaceArgs.put("--space", fullArgumentList.get("--space"));
    reflectSpaceArgs.put("--marker", fullArgumentList.get("--marker"));
    reflectSpaceArgs.put("--output", fullArgumentList.get("--output"));
    reflectSpaceArgs.put("--limit", fullArgumentList.get("--limit"));
    spaceCommands.put("reflect", new CliElement(reflectSpaceArgs, "reflect", "Placeholder"));

    HashMap<String, ArgumentItem> setRoleSpaceArgs = new HashMap<>();
    setRoleSpaceArgs.put("--space", fullArgumentList.get("--space"));
    setRoleSpaceArgs.put("--marker", fullArgumentList.get("--marker"));
    setRoleSpaceArgs.put("--email", fullArgumentList.get("--email"));
    setRoleSpaceArgs.put("--role", fullArgumentList.get("--role"));
    spaceCommands.put("set-role", new CliElement(setRoleSpaceArgs, "set-role", "Placeholder"));

    HashMap<String, ArgumentItem> generateKeySpaceArgs = new HashMap<>();
    generateKeySpaceArgs.put("--space", fullArgumentList.get("--space"));
    spaceCommands.put("generate-key", new CliElement(generateKeySpaceArgs, "generate-key", "Placeholder"));

    HashMap<String, ArgumentItem> encryptSecretSpaceArgs = new HashMap<>();
    encryptSecretSpaceArgs.put("--space", fullArgumentList.get("--space"));
    spaceCommands.put("encrypt-secret", new CliElement(encryptSecretSpaceArgs, "encrypt-secret", "Placeholder"));

    returnMap.put("space", new CliElement(spaceCommands, "space", "group", "Provides command related to working with space collections..."));

    HashMap<String, CliElement> authorityCommands = new HashMap<>();
    HashMap<String, ArgumentItem> createAuthorityArgs = new HashMap<>();
    authorityCommands.put("create", new CliElement(createAuthorityArgs, "create", "Placeholder"));

    HashMap<String, ArgumentItem> setAuthorityArgs = new HashMap<>();
    setAuthorityArgs.put("--authority", fullArgumentList.get("--authority"));
    setAuthorityArgs.put("--keystore", fullArgumentList.get("--keystore"));
    authorityCommands.put("set", new CliElement(setAuthorityArgs, "set", "Placeholder"));

    HashMap<String, ArgumentItem> getAuthorityArgs = new HashMap<>();
    getAuthorityArgs.put("--authority", fullArgumentList.get("--authority"));
    getAuthorityArgs.put("--keystore", fullArgumentList.get("--keystore"));
    authorityCommands.put("get", new CliElement(getAuthorityArgs, "get", "Placeholder"));

    HashMap<String, ArgumentItem> destroyAuthorityArgs = new HashMap<>();
    destroyAuthorityArgs.put("--authority", fullArgumentList.get("--authority"));
    authorityCommands.put("destroy", new CliElement(destroyAuthorityArgs, "destroy", "Placeholder"));

    HashMap<String, ArgumentItem> listAuthorityArgs = new HashMap<>();
    authorityCommands.put("list", new CliElement(listAuthorityArgs, "list", "Placeholder"));

    HashMap<String, ArgumentItem> createLocalAuthorityArgs = new HashMap<>();
    createLocalAuthorityArgs.put("--authority", fullArgumentList.get("--authority"));
    createLocalAuthorityArgs.put("--keystore", fullArgumentList.get("--keystore"));
    createLocalAuthorityArgs.put("--priv", fullArgumentList.get("--priv"));
    authorityCommands.put("create-local", new CliElement(createLocalAuthorityArgs, "create-local", "Placeholder"));

    HashMap<String, ArgumentItem> appendLocalAuthorityArgs = new HashMap<>();
    appendLocalAuthorityArgs.put("--authority", fullArgumentList.get("--authority"));
    appendLocalAuthorityArgs.put("--keystore", fullArgumentList.get("--keystore"));
    appendLocalAuthorityArgs.put("--priv", fullArgumentList.get("--priv"));
    authorityCommands.put("append-local", new CliElement(appendLocalAuthorityArgs, "append-local", "Placeholder"));

    HashMap<String, ArgumentItem> signAuthorityArgs = new HashMap<>();
    signAuthorityArgs.put("--key", fullArgumentList.get("--key"));
    signAuthorityArgs.put("--agent", fullArgumentList.get("--agent"));
    signAuthorityArgs.put("--validate", fullArgumentList.get("--validate"));
    authorityCommands.put("sign", new CliElement(signAuthorityArgs, "sign", "Placeholder"));

    returnMap.put("authority", new CliElement(authorityCommands, "authority", "group", "Placeholder"));

    return returnMap;
  }

  public static HashMap<String, ArgumentItem> populateArgs() {
    HashMap<String, ArgumentItem> argList = new HashMap<>();

    argList.put("--agent",new ArgumentItem("--agent", "-ag", "Placeholder"));

    argList.put("--role",new ArgumentItem("--role", "-r", "Placeholder"));

    argList.put("--space",new ArgumentItem("--space", "-s", "A 'space' is a collection of documents with the same schema and logic, and the 'space' parameter is used to denote the name of that collection."));

    argList.put("--output",new ArgumentItem("--output", "-o", "Placeholder"));

    argList.put("--file",new ArgumentItem("--file", "-f", "Placeholder"));

    argList.put("--marker",new ArgumentItem("--marker", "-m", "Placeholder"));

    argList.put("--authority",new ArgumentItem("--authority", "-a", "Placeholder"));

    argList.put("--limit",new ArgumentItem("--limit", "-l", "Placeholder"));

    argList.put("--keystore",new ArgumentItem("--keystore", "-k", "Placeholder"));

    argList.put("--priv",new ArgumentItem("--priv", "-p", "Placeholder"));

    argList.put("--gc",new ArgumentItem("--gc", "-g", "Placeholder"));

    argList.put("--plan",new ArgumentItem("--plan", "-p", "Placeholder"));

    argList.put("--key",new ArgumentItem("--key", "-k", "Placeholder"));

    argList.put("--email",new ArgumentItem("--email", "-e", "Placeholder"));

    argList.put("--validate",new ArgumentItem("--validate", "-v", "Placeholder"));

    return argList;
  }
}