/*
 * This file is subject to the terms and conditions outlined in the file 'LICENSE' (hint: it's MIT); this file is located in the root directory near the README.md which you should also read.
 *
 * This file is part of the 'Adama' project which is a programming language and document store for board games; however, it can be so much more.
 *
 * See http://www.adama-lang.org/ for more information.
 *
 * (c) 2020 - 2022 by Jeffrey M. Barber (http://jeffrey.io)
 */
package org.adamalang;

/** centralized listing of all error codes */
public class ErrorCodes {
  public static final int DURABLE_LIVING_DOCUMENT_STAGE_FRESH_DRIVE = 123392;
  public static final int DURABLE_LIVING_DOCUMENT_STAGE_FRESH_PERSIST = 198657;
  public static final int DURABLE_LIVING_DOCUMENT_STAGE_CONSTRUCT_DRIVE = 130568;
  public static final int DURABLE_LIVING_DOCUMENT_STAGE_CONSTRUCT_PERSIST = 134152;
  public static final int DURABLE_LIVING_DOCUMENT_STAGE_LOAD_DRIVE = 143880;
  public static final int DURABLE_LIVING_DOCUMENT_STAGE_LOAD_READ = 101386;
  public static final int DURABLE_LIVING_DOCUMENT_STAGE_ATTACH_PRIVATE_VIEW = 138255;
  public static final int LIVING_DOCUMENT_TRANSACTION_NO_COMMAND_FOUND = 194575;
  public static final int LIVING_DOCUMENT_TRANSACTION_UNRECOGNIZED_FIELD_PRESENT = 184335;
  public static final int LIVING_DOCUMENT_TRANSACTION_ALREADY_CONNECTED = 115724;
  public static final int LIVING_DOCUMENT_TRANSACTION_ALREADY_CONSTRUCTED = 132111;
  public static final int LIVING_DOCUMENT_TRANSACTION_CANT_DISCONNECT_DUE_TO_NOT_CONNECTED = 145423;
  public static final int LIVING_DOCUMENT_TRANSACTION_CANT_SEND_NO_CHANNEL = 160268;
  public static final int LIVING_DOCUMENT_TRANSACTION_CANT_SEND_NO_MESSAGE = 184332;
  public static final int LIVING_DOCUMENT_TRANSACTION_CANT_SEND_NOT_CONNECTED = 143373;
  public static final int LIVING_DOCUMENT_TRANSACTION_CANT_ATTACH_NOT_CONNECTED = 125966;
  public static final int LIVING_DOCUMENT_TRANSACTION_CLIENT_REJECTED = 184333;
  public static final int LIVING_DOCUMENT_TRANSACTION_NO_CLIENT_AS_WHO = 122896;
  public static final int LIVING_DOCUMENT_TRANSACTION_NO_LIMIT = 146448;
  public static final int LIVING_DOCUMENT_TRANSACTION_NO_CONSTRUCTOR_ARG = 196624;
  public static final int LIVING_DOCUMENT_TRANSACTION_NO_TIMESTAMP = 143889;
  public static final int LIVING_DOCUMENT_TRANSACTION_NO_VALID_COMMAND_FOUND = 132116;
  public static final int LIVING_DOCUMENT_TRANSACTION_NO_ASSET = 143380;
  public static final int LIVING_DOCUMENT_TRANSACTION_NO_PATCH = 193055;
  public static final int LIVING_DOCUMENT_TRANSACTION_MESSAGE_ALREADY_SENT = 143407;
  public static final int LIVING_DOCUMENT_TRANSACTION_EXPIRE_LIMIT_MUST_BE_POSITIVE = 122412;
  public static final int LIVING_DOCUMENT_TRANSACTION_EXPIRE_DID_NOTHING = 131203;
  public static final int FACTORY_CANT_BIND_JAVA_CODE = 198174;
  public static final int FACTORY_CANT_COMPILE_JAVA_CODE = 180258;
  public static final int FACTORY_CANT_CREATE_OBJECT_DUE_TO_CATASTROPHE = 115747;
  public static final int FACTORY_CANT_INVOKE_CAN_CREATE = 180858;
  public static final int FACTORY_CANT_INVOKE_CAN_SEND_WHILE_DISCONNECTED = 148095;
  public static final int FACTORY_CANT_INVOKE_CAN_INVENT = 146558;
  public static final int FACTORY_CANT_INVOKE_CONFIG = 117923;
  public static final int CATASTROPHIC_DOCUMENT_FAILURE_EXCEPTION = 144416;
  public static final int SERVICE_DOCUMENT_ALREADY_CREATED = 130092;
  public static final int SERVICE_DOCUMENT_REJECTED_CREATION = 134259;
  public static final int DOCUMENT_SELF_DESTRUCT_SUCCESSFUL = 134195;
  public static final int INMEMORY_DATA_GET_CANT_FIND_DOCUMENT = 198705;
  public static final int INMEMORY_DATA_INITIALIZED_UNABLE_ALREADY_EXISTS = 116787;
  public static final int INMEMORY_DATA_PATCH_CANT_FIND_DOCUMENT = 144944;
  public static final int INMEMORY_DATA_COMPUTE_CANT_FIND_DOCUMENT = 106546;
  public static final int INMEMORY_DATA_COMPUTE_PATCH_NOTHING_TODO = 120944;
  public static final int INMEMORY_DATA_COMPUTE_REWIND_NOTHING_TODO = 128052;
  public static final int INMEMORY_DATA_DELETE_CANT_FIND_DOCUMENT = 117816;
  public static final int INMEMORY_DATA_COMPUTE_INVALID_METHOD = 127034;
  public static final int INMEMORY_DATA_COMPACT_CANT_FIND_DOCUMENT = 103060;
  public static final int DOCUMENT_QUEUE_BUSY_TOO_MANY_PENDING_ITEMS = 123004;
  public static final int DOCUMENT_QUEUE_BUSY_WAY_BEHIND = 192639;
  public static final int DOCUMENT_QUEUE_CONFLICT_OPERATIONS = 159869;
  public static final int CORE_STREAM_CAN_ATTACH_UNKNOWN_EXCEPTION = 146569;

  public static final int UNCAUGHT_EXCEPTION_WEB_SOCKET = 295116;
  public static final int ONLY_ACCEPTS_TEXT_FRAMES = 213711;
  public static final int USERLAND_REQUEST_NO_METHOD_PROPERTY = 213708;
  public static final int USERLAND_REQUEST_NO_ID_PROPERTY = 233120;

  /**
   * all DataService implementations must use this for a patch failure due to sequencer out of whack
   */
  public static final int UNIVERSAL_PATCH_FAILURE_HEAD_SEQ_OFF = 621580;
  public static final int UNIVERSAL_LOOKUP_FAILED = 625676;
  public static final int UNIVERSAL_INITIALIZE_FAILURE = 667658;

  public static final int PATCH_FAILURE = 640009;
  public static final int COMPUTE_FAILURE = 605195;
  public static final int GET_FAILURE = 669710;
  public static final int DELETE_FAILURE = 641036;
  public static final int DEPLOYMENT_NOT_FOUND = 643084;
  public static final int COMPUTE_EMPTY_PATCH = 602115;
  public static final int COMPUTE_EMPTY_REWIND = 694287;
  public static final int COMPUTE_UNKNOWN_METHOD = 656396;

  public static final int FRONTEND_SPACE_ALREADY_EXISTS = 679948;
  public static final int FRONTEND_SPACE_DOESNT_EXIST = 625678;
  public static final int FRONTEND_PLAN_DOESNT_EXIST = 609294;
  public static final int FRONTEND_INTERNAL_PLAN_DOESNT_EXIST = 654341;
  public static final int INVALID_ROLE = 688141;
  public static final int FRONTEND_AUTHORITY_ALREADY_EXISTS = 601088;
  public static final int FRONTEND_AUTHORITY_SET_NOT_FOUNDOR_WRONG_OWNER = 634880;
  public static final int FRONTEND_AUTHORITY_CHANGE_OWNER_NOT_FOUND_OR_INCORRECT = 662528;
  public static final int FRONTEND_AUTHORITY_GET_NOT_FOUND_INTERNAL = 643072;
  public static final int FRONTEND_AUTHORITY_GET_NOT_FOUND_PUBLIC = 626691;
  public static final int FRONTEND_AUTHORITY_DELETE_NOT_FOUND_OR_INCORRECT = 654339;


  @User
  @Description("The deployment plan must be a object")
  public static final int DEPLOYMENT_PLAN_MUST_BE_ROOT_OBJECT = 117818;

  @User
  @Description("The deployment plan lacked a versions object within the root object")
  public static final int DEPLOYMENT_PLAN_NO_VERSIONS = 115788;
  public static final int DEPLOYMENT_PLAN_NO_DEFAULT = 143948;
  public static final int DEPLOYMENT_PLAN_VERSIONS_MUST_BE_OBJECT = 155711;
  public static final int DEPLOYMENT_PLAN_PLAN_MUST_BE_ARRAY = 126012;
  public static final int DEPLOYMENT_PLAN_PLAN_ARRAY_ELEMENT_MUST_OBJECT = 176703;
  public static final int DEPLOYMENT_PLAN_PERCENT_MUST_BE_DOUBLE = 151615;
  public static final int DEPLOYMENT_PLAN_VERSION_MUST_EXIST = 120895;
  public static final int DEPLOYMENT_PLAN_PLAN_NO_VERSION = 199768;
  public static final int DEPLOYMENT_PLAN_MUST_HAVE_DEFAULT = 145980;

  @User
  @Description("The given Adama file was unable to be parsed; see the associated errorJson()")
  public static final int DEPLOYMENT_CANT_PARSE_LANGUAGE = 117823;

  @User
  @Description("The given Adama file was unable to be typed; see associated errorJson()")
  public static final int DEPLOYMENT_CANT_TYPE_LANGUAGE = 132157;

  @User
  @Description("The space was not found on the given Adama host. Either this means the space doesn't exist, or a routing issue caused by a poor deployment or capacity management resulted in a invalid mapping")
  public static final int DEPLOYMENT_FACTORY_CANT_FIND_SPACE = 134214;
  public static final int DEPLOYMENT_UNKNOWN_FIELD_ROOT = 143430;
  public static final int DEPLOYMENT_UNKNOWN_FIELD_STAGE = 116812;
  public static final int DEPLOYMENT_UNKNOWN_EXCEPTION = 146561;

  /**
   * 127152 127155 133308 129727 114881 187586 127169 197827 168131 161987
   * 146115 194752 121027 130242 144583 146631 183498 199883 116936 115917 127692 199886 109775
   * 139469 128208 114384 145627 113884 197852 199907 191713 180978 147186 120048 177395 110832
   * 193267 193264 111347 197872 193265 131825 127732 162036 127736 133371 145659 134399 120060
   * 184063 125180 123644 130303 136444 165117 127745 182531 149251 148736 130819 100100 118020
   * 135940 131845 195851 131855 134927 122124 101135 135948 196876 130833 197905 195871 131356
   * 198434 144161 127780 131879 180004 134954 118573 134958 182578 101680 195891 103219 199472
   * 127794 148279 114484 199995 140088 143166 180031 142143 171839 115518 107328 131904 197447
   * 196939 196936 131919 183116 196448 110953 195951 123760 160115 145267 138096 131964 111487
   * 199043 115584 134016 115592 118669 116623 196492 180109 114582 135070 147363 144288 197548
   * 146354 163763 149427 114608 118707 147376 184258 163778 138179 142787 144320 180160 163776
   * 133056 127938 146881 177095 146887 143818 196555 193481 117709 151503 111564 118735 134099
   * 150483 151504 184280 188383 165852 118752 151011 123875 139745 183781 198639 189423 129007
   * 194547 193011 189424 133105 130040 193534 118781 142847 119804 167423 133119 131068 184319
   * 130559 148477
   */

  public static final int USER_NOT_FOUND_GET_PASSWORD = 684039;
  public static final int USER_NOT_FOUND_GET_BALANCE = 605208;

  /**
   * 605208 662552 643100 695327 647199 642079 604191 656403 634899 674832
   * 634900 643095 605227 639018 668719 684079 602158 642094 622624 605216 618532 605223 687163
   * 640056 654392 656443 639034 654394 684089 662591 603196 650300 666687 629822 691261 652350
   * 684082 620592 601136 658483 605235 678960 688176 635955 605234 651316 625716 688180 647244
   * 649292 606284 638028 691279 639052 655436 630851 605251 658500 639059 629868 639072 654459
   * 640124 653436 605311 656499 659568 622707 629900 671887 620687 665740 605327 602255 667789
   * 639105 670852 651416 655504 642235 654523 651453 622768 659632 653490 640183 646344 606411
   * 646347 654539 602317 641229 654540 655567 671951 601292 697548 681164 606401 603329 622787
   * 626883 602307 608450 642245 602308 688327 639175 651463 651462 662725 687323 652507 613596
   * 667859 645328 670928 688336 691408 658647 691412 687338 603368 688363 651499 687342 649452
   * 622828 667884 605422 651489 653536 657635 691424 659680 602339 604386 684263 655610 658682
   * 657658 688376 618749 625917 667903 651516 688383 654590 652542 616689 689394 644337 602353
   * 685299 669939 668915 680179 645360 670960 662768 675056 646386 696561 653554 670966 642292
   * 618743 652535 635126 642313 654600 675083 609548 670991 667916 691468 602382 657677 642311
   * 641311 675091 605484 625964 606511 675123 657712 639282 653620 651592 639311 615744 605506
   * 671091 666992 688496 668044 655746 675203 642439 639391 630174 627104 604576 643519 652735
   * 606640 672176 655792 658890 602568 640461 650701 656847 667072 640451 602562 655839 638428
   * 658900 658927 654831 695779 668154 647676 699888 651763 651762 642548 654861 641549 667151
   * 606735 647695 642575 658957 688642 635392 676355 646659 639491 623107 669185 671238 642576
   * 655912 604716 640559 658983 695844 639544 602683 655935 667199 606780 687676 684604 650814
   * 643633 691762 625200 672307 638515 685616 655927 654903 699993 606844 659087 669324 638592
   * 651907 602800 639666 671438 624332 641743 672460 656064 621251 668352 656065 672455 639696
   * 635628 656108 640743 611065 610040 656124 668413 667379 606963 688880 652018 672500 602889
   * 671498 639752 644872 684808 603915 668424 642826 684814 654093 664335 687887 660239 659212
   * 604943 654095 622351 639745 638720 673539 605952 626435 616194 668417 667396 646919 623384
   * 652059 671519 643868 652049 659218 663315 646931 666384 684816 652051 658193 642861 638765
   * 662319 641836 646956 688940 605999 652078 671522 655136 641824 660256 622371 639783 655161
   * 675640 647997 618300 660287 610111 607039 603953 623409 664370 626480 670515 694067 608048
   * 663344 668464 610099 658224 649011 684854 680759 603956 639799 602935 606006 675637 655176
   * 671567 602956 680783 646976 606016 651075 659283 655213 668526 601967 644988 646012 639871
   * 626559 652145 623472 648051 643954 655240 642955 647055 641934 654208 651136 623491 662400
   * 606083 656260 642968 675744 603064 689083 697279 675775 641983 691123 689075 663475 642996
   * 664503 622537 671691 601035 660430 638925 695246 628684 656332 623567 652239 653263 651215
   * 670668 608207 667597 640974 639937 619456 690115 669635 662467 624579 623555 692160 648130
   * 630722 652231 673732 693188 603096 654300 648159 625631 638942 655314 689108 651240 672751
   * 663532 638959 626670 659427 603107 635879 619512 668665 643066 623613 605180 669695 696316
   * 656380 635903 625663 684029 653310 652286 605182 692221 654321 641008 685043 606195 626675
   * 698352 641010 635893 605172 691188
   */
  public static final int AUTH_FAILED_FINDING_DEVELOPER_KEY = 966671;
  public static final int AUTH_FAILED_VALIDATING_AGAINST_KEYSTORE = 916531;
  public static final int AUTH_UNKNOWN_EXCEPTION = 973839;
  public static final int AUTH_INVALID_TOKEN_LAYOUT = 995342;
  public static final int AUTH_INVALID_TOKEN_JSON = 908303;
  public static final int AUTH_INVALID_TOKEN_JSON_COMPLETE = 959500;
  public static final int USERID_RESOLVE_UNKNOWN_EXCEPTION = 979980;
  public static final int API_INVALID_EMAIL = 905293;
  public static final int SPACE_POLICY_LOCATOR_UNKNOWN_EXCEPTION = 969741;

  public static final int API_SPACE_INVALID_NAME_FOR_LOOKUP = 928828;

  public static final int API_SPACE_CREATE_UNKNOWN_EXCEPTION = 900104;

  public static final int API_INIT_SETUP_UNKNOWN_EXCEPTION = 965636;
  public static final int API_INIT_COMPLETE_UNKNOWN_EXCEPTION = 946179;
  public static final int API_INIT_COMPLETE_CODE_MISMATCH = 916486;

  public static final int API_SPACE_SET_PLAN_NO_PERMISSION_TO_EXECUTE = 901127;
  public static final int API_SPACE_SET_PLAN_UNKNOWN_EXCEPTION = 904318;
  public static final int API_SPACE_SET_PLAN_DEPLOYMENT_FAILED_FINDING_CAPACITY = 965747;

  public static final int API_SPACE_GET_PLAN_NO_PERMISSION_TO_EXECUTE = 965635;
  public static final int API_SPACE_GET_PLAN_UNKNOWN_EXCEPTION = 913408;

  public static final int API_SPACE_GET_BILLING_USAGE_NO_PERMISSION_TO_EXECUTE = 909452;
  public static final int API_SPACE_GET_BILLING_UNKNOWN_EXCEPTION = 901263;

  public static final int API_SPACE_SET_ROLE_UNKNOWN_EXCEPTION = 986120;
  public static final int API_SPACE_SET_ROLE_NO_PERMISSION_TO_EXECUTE = 921607;

  public static final int API_SPACE_LIST_UNKNOWN_EXCEPTION = 941064;
  public static final int API_SPACE_LIST_NO_PERMISSION_TO_EXECUTE = 920576;

  public static final int API_SPACE_REFLECT_NO_PERMISSION_TO_EXECUTE = 907343;

  public static final int API_CREATE_AUTHORITY_UNKNOWN_EXCEPTION = 982016;
  public static final int API_CREATE_AUTHORITY_NO_PERMISSION_TO_EXECUTE = 990208;

  public static final int API_SET_AUTHORITY_UNKNOWN_EXCEPTION = 900098;
  public static final int API_SET_AUTHORITY_NO_PERMISSION_TO_EXECUTE = 970780;

  public static final int API_GET_AUTHORITY_UNKNOWN_EXCEPTION = 928819;
  public static final int API_GET_AUTHORITY_NO_PERMISSION_TO_EXECUTE = 978992;

  public static final int API_LIST_AUTHORITY_UNKNOWN_EXCEPTION = 998430;
  public static final int API_LIST_AUTHORITY_NO_PERMISSION_TO_EXECUTE = 904223;

  public static final int API_DELETE_AUTHORITY_UNKNOWN_EXCEPTION = 913436;
  public static final int API_DELETE_AUTHORITY_NO_PERMISSION_TO_EXECUTE = 901144;

  public static final int API_CREATE_DOCUMENT_UNKNOWN_EXCEPTION = 933907;

  public static final int API_SEND_TIMEOUT = 984080;
  public static final int API_SEND_REJECTED = 916520;

  public static final int API_UPDATE_TIMEOUT = 997516;
  public static final int API_UPDATE_REJECTED = 998539;

  public static final int API_CAN_ATTACH_TIMEOUT = 984111;
  public static final int API_ATTACH_TIMEOUT = 916527;

  public static final int API_CAN_ATTACH_REJECTED = 901163;
  public static final int API_ATTACH_REJECTED = 913447;

  public static final int INSTANCE_FINDER_TIMEOUT = 998434;
  public static final int INSTANCE_FINDER_REJECTED = 930848;
  public static final int STATE_MACHINE_TOO_MANY_FAILURES = 992319;
  public static final int STATE_MACHINE_UNABLE_TO_RECONNECT = 947263;

  public static final int API_DEPLOY_REJECTED = 901180;
  public static final int API_DEPLOY_TIMEOUT = 912444;

  public static final int API_CREATE_CANT_FIND_CAPACITY = 912447;
  public static final int API_CREATE_TIMEOUT = 962655;
  public static final int API_CREATE_REJECTED = 996436;

  public static final int API_REFLECT_CANT_FIND_CAPACITY = 969806;
  public static final int API_REFLECT_TIMEOUT = 902223;
  public static final int API_REFLECT_REJECTED = 983117;

  public static final int API_METHOD_NOT_FOUND = 945213;
  public static final int API_INVALID_KEY_EMPTY = 919676;
  public static final int API_INVALID_KEY_NOT_SIMPLE = 946192;

  public static final int API_INVALID_SPACE_EMPTY = 937076;
  public static final int API_INVALID_SPACE_NOT_SIMPLE = 998515;

  public static final int API_KEYSTORE_NOT_JSON = 998459;
  public static final int API_KEYSTORE_ROOT_ITEM_NOT_OBJECT = 949307;
  public static final int API_KEYSTORE_KEY_LACKS_BYTES64 = 901179;
  public static final int API_KEYSTORE_KEY_LACKS_VALID_BYTES64 = 987191;

  public static final int API_KEYSTORE_KEY_LACKS_ALGO = 967735;
  public static final int API_KEYSTORE_KEY_LACKS_VALID_ALGO = 907319;
  public static final int API_KEYSTORE_KEY_INTERNAL_ERROR = 952372;

  public static final int API_ASSET_UPLOAD_FAILED = 950322;
  public static final int API_ASSET_FAILED_BIND = 919601;
  public static final int API_ASSET_CHUNK_BAD_DIGEST = 999472;
  public static final int API_ASSET_CHUNK_UNKNOWN_EXCEPTION = 994352;
  public static final int API_ASSET_ATTACHMENT_NOT_ALLOWED = 966768;
  public static final int API_ASSET_ATTACHMENT_LOST_CONNECTION = 920719;

  public static final int API_LIST_DOCUMENTS_UNKNOWN_EXCEPTION = 903232;
  public static final int API_LIST_DOCUMENTS_NO_PERMISSION = 900160;
  public static final int API_SPACE_DELETE_UNKNOWN_EXCEPTION = 904256;
  public static final int API_SPACE_DELETE_NO_PERMISSION = 904285;
  public static final int API_SPACE_DELETE_NOT_EMPTY = 998495;

  public static final int API_METERING_FAILED_FINDING_RANDOM_HOST = 909436;
  public static final int API_METERING_TIMEOUT = 998480;
  public static final int API_METERING_REJECTED = 998499;

  public static final int API_CHANNEL_VALIDATION_FAILED_EMPTY = 950399;
  public static final int API_CHANNEL_VALIDATION_BAD_START_CHARACTER = 908415;
  public static final int API_CHANNEL_VALIDATION_BAD_MIDDLE_CHARACTER = 967804;

  public static final int API_GOODWILL_EXCEPTION = 950384;

  public static final int AWS_EMAIL_SEND_FAILURE = 901232;

  /**
   * 991368 904327 985219 901251 985216 983199
   * 904348 904343 928915 925840 908435 904364 912544 928959 904383 966835 949427 978099 995505
   * 928944 903347 986319 993487 913615 947404 904399 903375 937164 984268 924877 991435 904392
   * 979145 904394 920777 929990 998599 979143 967879 903364 931015 982212 986308 950469 921794
   * 999619 929987 977091 934083 962752 998593 914626 950465 919774 921823 928991 909532 973020
   * 988380 966875 916689 920787 990417 942318 990447 915695 980204 917740 982253 995563 920811
   * 966888 901348 998625 998624 993504 997631 994558 983294 902399 994556 979194 999675 909563
   * 984312 930039 914676 913655 903415 996596 978162 989427 970995 918771 999665 901363 904461
   * 997647 971023 934159 903435 969987 946435 924928 982272 986396 982291 901421 930111 922943
   * 901428 955700 991540 904502 998708 995636 986419 966962 916784 903475 904499 930097 995663
   * 933196 998732 995695 970108 916924 998839 932279 995763 921008 996784 920015 995788 904648
   * 981451 968135 990658 946627 904643 916930 983506 903660 967148 998892 986594 983551 913916
   * 970239 901628 933372 902655 918011 933368 998904 913908 913904 975344 991757 933388 903695
   * 918027 995844 998915 967170 917020 983587 984609 909884 980543 913983 971324 998967 983604
   * 901680 967216 900659 979535 909903 901743 914046 990863 982671 915118 996031 931516 914111
   * 975548 984755 904880 904909 975567 983757 925388 904897 910016 983746 901827 950976 917186
   * 914140 901852 967379 913107 909039 921324 999139 934654 910077 970495 915196 999155 984818
   * 901872 990960 904946 987888 995087 922383 999182 982799 905996 978703 900876 997133 981772
   * 900879 918285 996107 931595 901899 979721 917258 914181 926471 917255 934658 917249 954115
   * 959235 981763 904963 996096 992000 998144 989952 921374 989983 967455 991006 986905 982808
   * 982804 930578 979730 904977 960272 933648 903955 992016 986896 917266 921388 996141 992044
   * 914212 985891 901922 967486 947007 975676 910143 974653 971574 917300 982839 966452 931637
   * 978738 906033 980787 985906 986930 925488 916275 915250 997199 919372 916303 997196 905032
   * 909120 916288 964419 917340 905055 918360 929616 977745 999272 999271 921442 933731 906108
   * 979839 918396 931708 909183 903038 928635 987000 905077 979829 904051 985968 930673 995184
   * 997263 914319 982924 906123 914308 999301 987012 983940 983938 995200 983952 979885 997311
   * 906172 917439 909247 914360 991159 901041 904112 909235 926640 930736 985039 921550 930766
   * 942031 992206 921551 904143 988109 950220 954317 997320 992200 918470 984007 921540 986052
   * 933826 966595 985026 969667 930755 979907 917443 933824 996288 905180 967644 918492 987092
   * 984019 954323 930771 999378 950224 930768 984047 918508 917486 984044 914403 999423 917501
   * 906236 937980 916479 995324 967677 918525 984059 987131 967675 917496 969720 967672 988152
   * 930807 994291 901104 967667 991218 998385 998384
   */
  public static final int GRPC_COMMON_FAILED_TO_FIND_STREAM_USING_GIVEN_ACT = 798735;

  public static final int GRPC_SEND_FAILED_NOT_CONNECTED = 777231;
  public static final int GRPC_ASK_FAILED_NOT_CONNECTED = 701452;
  public static final int GRPC_ATTACHED_FAILED_NOT_CONNECTED = 786442;
  public static final int GRPC_UPDATE_FAILED_NOT_CONNECTED = 790528;
  public static final int GRPC_CREATE_UNKNOWN_EXCEPTION = 723982;
  public static final int GRPC_DISCONNECT = 786441;
  public static final int GRPC_FAILURE = 716812;
  public static final int GRPC_REFLECT_UNKNOWN_EXCEPTION = 791567;
  public static final int GRPC_METERING_UNKNOWN_EXCEPTION = 782348;
  public static final int GRPC_METERING_UNEXPECTED_ERROR = 786436;
  public static final int GRPC_HANDLER_EXCEPTION = 734211;
  public static final int GRPC_HANDLER_SCAN_EXCEPTION = 716806;

  public static final int GRPC_STREAM_ASK_TIMEOUT = 774147;
  public static final int GRPC_STREAM_ASK_REJECTED = 782339;

  public static final int GRPC_STREAM_ATTACH_TIMEOUT = 751618;
  public static final int GRPC_STREAM_ATTACH_REJECTED = 733185;

  public static final int GRPC_STREAM_SEND_TIMEOUT = 768000;
  public static final int GRPC_STREAM_SEND_REJECTED = 754688;

  public static final int GRPC_STREAM_UPDATE_TIMEOUT = 786433;
  public static final int GRPC_STREAM_UPDATE_REJECTED = 796674;

  public static final int DISK_GET_IO_EXCEPTION = 716804;
  public static final int DISK_INITIALIZE_IO_EXCEPTION = 793602;
  public static final int DISK_UNABLE_TO_PATCH_FILE_NOT_FOUND = 794627;
  public static final int DISK_PATCH_IO_EXCEPTION = 777244;
  public static final int DISK_UNABLE_TO_DELETE = 769042;
  public static final int DISK_UNABLE_TO_COMPACT_FILE_NOT_FOUND = 784401;
  public static final int DISK_UNABLE_TO_COMPACT_NON_POSITIVE_HISTORY = 777259;
  public static final int DISK_COMPACT_READ_IO_EXCEPTION = 736272;
  public static final int DISK_COMPACT_WRITE_IO_EXCEPTION = 739351;
  public static final int DISK_UNABLE_TO_COMPUTE_FILE_NOT_FOUND = 790544;

  public static final int DISK_COMPUTE_HEADPATCH_NOTHING_TO_DO = 725039;
  public static final int DISK_COMPUTE_HEADPATCH_IOEXCEPTION = 773164;

  public static final int DISK_COMPUTE_REWIND_NOTHING_TO_DO = 783395;
  public static final int DISK_COMPUTE_REWIND_IOEXCEPTION = 724012;
  public static final int DISK_COMPUTE_INVALID_METHOD = 705583;

  public static final int NET_FAILED_INITIATION = 753699;
  public static final int NET_DISCONNECT = 773155;
  public static final int NET_SHUTTING_DOWN = 788515;
  public static final int NET_CONNECT_FAILED_UNKNOWN = 798756;
  public static final int NET_CONNECT_FAILED_TO_CONNECT = 724001;

  public static final int ADAMA_NET_PING_TIMEOUT = 773152;
  public static final int ADAMA_NET_PING_REJECTED = 786466;
  public static final int ADAMA_NET_CREATE_TIMEOUT = 720955;
  public static final int ADAMA_NET_CREATE_REJECTED = 737336;
  public static final int ADAMA_NET_CLOSE_TIMEOUT = 788543;
  public static final int ADAMA_NET_CLOSE_REJECTED = 796735;
  public static final int ADAMA_NET_METERING_TIMEOUT = 770104;
  public static final int ADAMA_NET_METERING_REJECTED = 786495;
  public static final int ADAMA_NET_SCAN_DEPLOYMENT_TIMEOUT = 715839;
  public static final int ADAMA_NET_SCAN_DEPLOYMENT_REJECTED = 787514;
  public static final int ADAMA_NET_CONNECT_DOCUMENT_TIMEOUT = 718908;
  public static final int ADAMA_NET_CONNECT_DOCUMENT_REJECTED = 702524;
  public static final int ADAMA_NET_CONNECTION_DONE = 769085;
  public static final int ADAMA_NET_INVALID_TARGET = 719932;
  public static final int ADAMA_NET_FAILED_FIND_TARGET = 753724;


  /**
   *   797755
   * 783411 724019 703539 792631 732208 734263 791602 787507 798796 786509 716879 735308 771139
   * 707651 792643 736347 799836 707675 790622 790623 785491 785490 788560 773242 786557 785528
   * 788607 773247 777331 722034 716912 735344 734320 768112 789645 793742 739471 733327 720012
   * 791691 717952 797824 735364 716947 798890 789668 786620 787645 793790 773309 703667 786615
   * 772272 770224 787632 790704 797872 793779 790732 752841 750799 787656 729294 775372 776396
   * 781507 752835 733378 785601 798918 778433 716993 784576 787655 725184 756928 740544 721095
   * 711876 704708 785604 723140 790748 736472 781535 789720 729311 722131 774354 787664 787692
   * 788717 707816 797934 793839 735471 752879 766191 787684 798944 793824 797923 736484 752891
   * 782585 749821 782589 707836 778492 758012 736508 798971 777459 784627 719091 705779 772337
   * 751857 756976 720113 746736 725239 770295 796912 708855 777460 797939 798963 736500 785673
   * 769295 722188 777491 797971 717103 732460 799012 790819 793916 799037 788798 721208 789823
   * 737585 724279 782671 707916 794972 799086 773484 736635 785791 782719 789879 798064 789874
   * 705920 799104 789891 770463 789920 786866 795085 786893 773576 782798 799178 774604 799172
   * 794052 708032 774592 773574 733672 782831 707040 725472 786940 799228 795134 786936 782847
   * 740851 736752 791024 702964 796147 791027 795150 785933 770572 750092 769548 754188 736780
   * 757251 733696 797184 770567 721412 790019 769552 773648 799249 790035 708128 793120 790049
   * 799267 770619 717374 774716 781875 719411 724528 788016 790064 789041 797235 705100 717376
   * 744038 774775 795251 786050 772771 706214 746150 799423 798414 721615 701135 720588 790219
   * 701120 705217 736964 773852 790224 790252 798444 705263 795391 766719 784126 733948 790260
   * 708336 789238 788237 720648 789262 797455 790287 733967 736015 777999 726799 751374 771853
   * 708365 724748 788228 729859 799492 716547 790277 717571 762625 705281 728832 797440 737031
   * 790273 796419 791299 787231 733983 773919 770835 783123 782097 786192 720661 737067 797484
   * 736040 798504 788264 757550 799530 774957 783139 704288 771899 795455 769848 778040 779071
   * 768831 782143 723775 798520 756543 725823 786239 724799 718652 774963 737075 711475 762673
   * 718640 728880 750384 705329 796464 774967 773943 770869 787250 770895 774991 779084 739148
   * 790342 741184 783187 793425 771948 705400 786296 788351 794488 721791 786303 724860 746364
   * 789360 796531 787315 799628 789390 783247 798600 773007 712591 788361 774028 725900 790403
   * 787331 799640 782239 770991 705452 724908 787361 790462 789433 784316 757683 753587 721841
   * 786359 787404 797644 732107 791501 782280 753608 774088 741320 792527 753615 745423 785358
   * 784334 786382 795594 721868 735180 781260 779212 720835 787397 791493 790471 721856 786375
   * 789441 709575 794562 792515 749508 720859 787423 779231 705503 798683 753628 737244 712659
   * 783313 734160 795601 799725 788463 736239 779247 742383 784367 723951 705517 770028 705507
   * 720867 717792 720871 799714 726011 796668 717816 740351 705535 785405 717820 712700 753651
   * 728051 736243 783347 774130 737266 789495 709617 758768 799728 787440
   */
}
