package com.jdchain.samples.sdk;

import com.jd.blockchain.contract.OnLineContractProcessor;
import com.jd.blockchain.crypto.Crypto;
import com.jd.blockchain.crypto.HashDigest;
import com.jd.blockchain.ledger.BlockchainIdentity;
import com.jd.blockchain.ledger.BytesValue;
import com.jd.blockchain.ledger.ConsensusSettingsUpdateOperation;
import com.jd.blockchain.ledger.ContractCodeDeployOperation;
import com.jd.blockchain.ledger.ContractEventSendOperation;
import com.jd.blockchain.ledger.ContractInfo;
import com.jd.blockchain.ledger.DataAccountInfo;
import com.jd.blockchain.ledger.DataAccountKVSetOperation;
import com.jd.blockchain.ledger.DataAccountRegisterOperation;
import com.jd.blockchain.ledger.DigitalSignature;
import com.jd.blockchain.ledger.Event;
import com.jd.blockchain.ledger.EventAccountRegisterOperation;
import com.jd.blockchain.ledger.EventPublishOperation;
import com.jd.blockchain.ledger.KVDataVO;
import com.jd.blockchain.ledger.KVInfoVO;
import com.jd.blockchain.ledger.LedgerAdminInfo;
import com.jd.blockchain.ledger.LedgerBlock;
import com.jd.blockchain.ledger.LedgerInfo;
import com.jd.blockchain.ledger.LedgerInitOperation;
import com.jd.blockchain.ledger.LedgerMetadata;
import com.jd.blockchain.ledger.LedgerPermission;
import com.jd.blockchain.ledger.LedgerTransaction;
import com.jd.blockchain.ledger.Operation;
import com.jd.blockchain.ledger.ParticipantNode;
import com.jd.blockchain.ledger.ParticipantRegisterOperation;
import com.jd.blockchain.ledger.PrivilegeSet;
import com.jd.blockchain.ledger.RolesConfigureOperation;
import com.jd.blockchain.ledger.TransactionContent;
import com.jd.blockchain.ledger.TransactionPermission;
import com.jd.blockchain.ledger.TransactionRequest;
import com.jd.blockchain.ledger.TransactionResult;
import com.jd.blockchain.ledger.TransactionState;
import com.jd.blockchain.ledger.TypedKVEntry;
import com.jd.blockchain.ledger.UserAuthorizeOperation;
import com.jd.blockchain.ledger.UserInfo;
import com.jd.blockchain.ledger.UserPrivilegeSet;
import com.jd.blockchain.ledger.UserRegisterOperation;
import org.junit.Assert;
import org.junit.Test;
import utils.Property;
import utils.codec.Base58Utils;
import utils.io.BytesUtils;

import java.util.Arrays;

/**
 * ????????????
 */
public class QuerySample extends SampleBase {

    HashDigest sampleHash = Crypto.resolveAsHashDigest(Base58Utils.decode("j5sTuEAWmLWKFwXgpdUCxbQN1XmZfkQdC94UT2AqQEt7hp"));
    String sampleUserAddress = "LdeNr7H1CUbqe3kWjwPwiqHcmd86zEQz2VRye";
    String sampleDataAccountAddress = "LdeNr7H1CUbqe3kWjwPwiqHcmd86zEQz2VRye";
    String sampleContractAddress = "LdeNr7H1CUbqe3kWjwPwiqHcmd86zEQz2VRye";
    String sampleEventAddress = "LdeNr7H1CUbqe3kWjwPwiqHcmd86zEQz2VRye";
    String sampleKey = "sample-key";
    String sampleEvent = "sample-event";
    long sampleVersion = 0;
    String sampleRoleName = "SAMPLE-ROLE";

    /**
     * ??????????????????
     */
    @Test
    public void getLedgerHashs() {
        HashDigest[] digests = blockchainService.getLedgerHashs();
        for (HashDigest digest : digests) {
            System.out.println(digest);
        }
    }

    /**
     * ???????????????????????????hash???????????????
     */
    @Test
    public void getLedger() {
        LedgerInfo ledgerInfo = blockchainService.getLedger(ledger);
        // ????????????
        System.out.println(ledgerInfo.getHash());
        // ??????????????????
        System.out.println(ledgerInfo.getLatestBlockHash());
        // ??????????????????
        System.out.println(ledgerInfo.getLatestBlockHeight());
    }

    /**
     * ????????????????????????????????????????????????????????????
     */
    @Test
    public void getLedgerAdminInfo() {
        LedgerAdminInfo adminInfo = blockchainService.getLedgerAdminInfo(ledger);
        System.out.println(adminInfo.getParticipantCount());
    }

    /**
     * ?????????????????????
     */
    @Test
    public void getConsensusParticipants() {
        ParticipantNode[] nodes = blockchainService.getConsensusParticipants(ledger);
        for (ParticipantNode node : nodes) {
            System.out.println("ID: " + node.getId());
            System.out.println("Address: " + node.getAddress().toString());
            System.out.println("PubKey: " + node.getPubKey().toString());
            System.out.println("State: " + node.getParticipantNodeState());
        }
    }

    /**
     * ????????????????????????
     */
    @Test
    public void getLedgerMetadata() {
        LedgerMetadata metadata = blockchainService.getLedgerMetadata(ledger);
        System.out.println(Base58Utils.encode(metadata.getSeed()));
        System.out.println(metadata.getParticipantsHash().toBase58());
        System.out.println(metadata.getSettingsHash().toBase58());
    }

    /**
     * ????????????????????????
     */
    @Test
    public void getBlockByHeight() {
        LedgerBlock block1 = blockchainService.getBlock(ledger, -1);
        // ????????????
        System.out.println(block1.getLedgerHash());
        // ????????????
        System.out.println(block1.getHeight());
        // ????????????
        System.out.println(block1.getTimestamp());
        // ????????????
        System.out.println(block1.getHash());
        // ??????????????????
        System.out.println(block1.getPreviousHash());
        // ????????????????????????
        System.out.println(block1.getTransactionSetHash());
        // ????????????????????????????????????
        System.out.println(block1.getAdminAccountHash());
        // ????????????????????????
        System.out.println(block1.getContractAccountSetHash());
        // ????????????????????????
        System.out.println(block1.getDataAccountSetHash());
        // ????????????????????????
        System.out.println(block1.getSystemEventSetHash());
        // ????????????????????????
        System.out.println(block1.getUserAccountSetHash());
        // ???????????????????????????
        System.out.println(block1.getUserEventSetHash());

        LedgerBlock block2 = blockchainService.getBlock(ledger, Integer.MAX_VALUE);
        Assert.assertNotNull(block1);
        Assert.assertEquals(block1.getHash(), block2.getHash());
        LedgerBlock block3 = blockchainService.getBlock(ledger, 0);
        Assert.assertTrue(block1.getHeight() >= block3.getHeight());
    }

    /**
     * ??????hash????????????
     */
    @Test
    public void getBlockByHash() {
        LedgerBlock block = blockchainService.getBlock(ledger, sampleHash);
        Assert.assertNull(block);
    }

    /**
     * ???????????????????????????????????????????????????
     */
    @Test
    public void getTransactionCountByHeight() {
        long count = blockchainService.getTransactionCount(ledger, -1);
        Assert.assertEquals(0, count);
        count = blockchainService.getTransactionCount(ledger, 1);
        Assert.assertNotEquals(0, count);
    }

    /**
     * ???????????????????????????????????????????????????
     */
    @Test
    public void getTransactionCountByHash() {
        long count = blockchainService.getTransactionCount(ledger, sampleHash);
        Assert.assertEquals(0, count);
    }

    /**
     * ??????????????????
     */
    @Test
    public void getTransactionTotalCount() {
        long count = blockchainService.getTransactionTotalCount(ledger);
        Assert.assertNotEquals(0, count);
    }

    /**
     * ???????????????????????????????????????????????????
     */
    @Test
    public void getDataAccountCountByHeight() {
        long count = blockchainService.getDataAccountCount(ledger, 0);
        Assert.assertEquals(0, count);
    }

    /**
     * ???????????????????????????????????????????????????
     */
    @Test
    public void getDataAccountCountByHash() {
        long count = blockchainService.getDataAccountCount(ledger, sampleHash);
        Assert.assertEquals(0, count);
    }

    /**
     * ????????????????????????
     */
    @Test
    public void getDataAccountTotalCount() {
        long count = blockchainService.getDataAccountTotalCount(ledger);
        System.out.println("Total DataAccount count: " + count);
    }

    /**
     * ?????????????????????????????????????????????
     */
    @Test
    public void getUserCountByHeight() {
        long count = blockchainService.getUserCount(ledger, 0);
        Assert.assertEquals(4, count);
    }

    /**
     * ?????????????????????????????????????????????
     */
    @Test
    public void getUserCountByHash() {
        long count = blockchainService.getUserCount(ledger, sampleHash);
        Assert.assertEquals(0, count);
    }

    /**
     * ??????????????????
     */
    @Test
    public void getUserTotalCount() {
        long count = blockchainService.getUserTotalCount(ledger);
        System.out.println("Total User count: " + count);
    }

    /**
     * ?????????????????????????????????????????????
     */
    @Test
    public void getContractCountByHeight() {
        long count = blockchainService.getContractCount(ledger, 0);
        Assert.assertEquals(0, count);
    }

    /**
     * ?????????????????????????????????????????????
     */
    @Test
    public void getContractCountByHash() {
        long count = blockchainService.getContractCount(ledger, sampleHash);
        Assert.assertEquals(0, count);
    }

    /**
     * ??????????????????
     */
    @Test
    public void getContractTotalCount() {
        long count = blockchainService.getContractTotalCount(ledger);
        System.out.println("Total Contract count: " + count);
    }

    /**
     * ???????????????????????????????????????????????????????????????
     */
    @Test
    public void getTransactionsByHeight() {
        LedgerTransaction[] txs = blockchainService.getTransactions(ledger, 0, 0, 1);
        Assert.assertEquals(1, txs.length);
    }

    /**
     * ???????????????????????????????????????????????????????????????
     */
    @Test
    public void getTransactionsByHash() {
        LedgerTransaction[] txs = blockchainService.getTransactions(ledger,
                sampleHash, 0, 1);
        Assert.assertNull(txs);
    }

    /**
     * ????????????????????????????????????
     */
    @Test
    public void getAdditionalTransactionsByHeight() {
        LedgerTransaction[] txs = blockchainService.getAdditionalTransactions(ledger, 0, 0, 1);
        Assert.assertEquals(1, txs.length);
        for (LedgerTransaction tx : txs) {
            /**
             * ??????????????????
             */
            TransactionResult result = tx.getResult();
            // ??????????????????
            System.out.println(result.getExecutionState());
            // ????????????????????????
            System.out.println(result.getBlockHeight());
            /**
             * ??????????????????
             */
            TransactionRequest request = tx.getRequest();
            // ????????????
            System.out.println(request.getTransactionHash());
            // ????????????????????????
            DigitalSignature[] endpointSignatures = request.getEndpointSignatures();
            for (DigitalSignature signature : endpointSignatures) {
                // ??????
                System.out.println(signature.getDigest());
                // ??????
                System.out.println(signature.getPubKey());
            }
            // ??????????????????
            DigitalSignature[] nodeSignatures = request.getNodeSignatures();
            for (DigitalSignature signature : nodeSignatures) {
                // ??????
                System.out.println(signature.getDigest());
                // ??????
                System.out.println(signature.getPubKey());
            }
            // ????????????
            TransactionContent transactionContent = request.getTransactionContent();
            transactionContent.getTimestamp(); // ????????????????????????????????????????????????
            Operation[] operations = transactionContent.getOperations(); // ????????????
            for (Operation operation : operations) {
                if (operation instanceof UserRegisterOperation) { // ????????????
                    UserRegisterOperation userRegisterOperation = (UserRegisterOperation) operation;
                    // ??????
                    System.out.println(userRegisterOperation.getUserID().getAddress());
                    //??????
                    System.out.println(userRegisterOperation.getUserID().getPubKey());
                } else if (operation instanceof DataAccountRegisterOperation) { // ??????????????????
                    DataAccountRegisterOperation dataAccountRegisterOperation = (DataAccountRegisterOperation) operation;
                    // ??????
                    System.out.println(dataAccountRegisterOperation.getAccountID().getAddress());
                    // ??????
                    System.out.println(dataAccountRegisterOperation.getAccountID().getPubKey());
                } else if (operation instanceof ContractCodeDeployOperation) { // ????????????
                    ContractCodeDeployOperation contractCodeDeployOperation = (ContractCodeDeployOperation) operation;
                    // ??????
                    System.out.println(contractCodeDeployOperation.getContractID().getAddress());
                    // ??????
                    System.out.println(contractCodeDeployOperation.getContractID().getPubKey());
                    // ????????????
                    System.out.println(OnLineContractProcessor.getInstance().decompileEntranceClass(contractCodeDeployOperation.getChainCode()));
                    // ????????????
                    System.out.println(contractCodeDeployOperation.getChainCodeVersion());
                } else if (operation instanceof EventAccountRegisterOperation) { // ??????????????????
                    EventAccountRegisterOperation eventAccountRegisterOperation = (EventAccountRegisterOperation) operation;
                    // ??????
                    System.out.println(eventAccountRegisterOperation.getEventAccountID().getAddress());
                    // ??????
                    System.out.println(eventAccountRegisterOperation.getEventAccountID().getPubKey());
                } else if (operation instanceof DataAccountKVSetOperation) { // ??????kv
                    DataAccountKVSetOperation kvSetOperation = (DataAccountKVSetOperation) operation;
                    // ??????????????????
                    System.out.println(kvSetOperation.getAccountAddress());
                    // ??????kv??????
                    DataAccountKVSetOperation.KVWriteEntry[] kvs = kvSetOperation.getWriteSet();
                    for (DataAccountKVSetOperation.KVWriteEntry kv : kvs) {
                        // key
                        System.out.println(kv.getKey());
                        // ??????????????????????????????
                        System.out.println(kv.getExpectedVersion());
                        // value
                        BytesValue value = kv.getValue();
                        switch (value.getType()) {
                            case TEXT:
                            case XML:
                            case JSON:
                                System.out.println(value.getBytes().toString());
                                break;
                            case INT64:
                            case TIMESTAMP:
                                System.out.println(BytesUtils.toLong(value.getBytes().toBytes()));
                                break;
                            default: // byte[], Bytes, IMG
                                System.out.println(value.getBytes());
                                break;
                        }
                    }
                } else if (operation instanceof ContractEventSendOperation) { // ????????????
                    ContractEventSendOperation contractEventSendOperation = (ContractEventSendOperation) operation;
                    // ????????????
                    System.out.println(contractEventSendOperation.getContractAddress());
                    // ????????????
                    System.out.println(contractEventSendOperation.getEvent());
                    // ????????????
                    for (BytesValue arg : contractEventSendOperation.getArgs().getValues()) {
                        switch (arg.getType()) {
                            case TEXT:
                                System.out.println(BytesUtils.toString(arg.getBytes().toBytes()));
                                break;
                            case INT64:
                                System.out.println(BytesUtils.toLong(arg.getBytes().toBytes()));
                                break;
                            case BOOLEAN:
                                System.out.println(BytesUtils.toBoolean(arg.getBytes().toBytes()[0]));
                                break;
                            case BYTES:
                                System.out.println(arg.getBytes().toBytes());
                            default:
                                break;
                        }
                    }
                } else if (operation instanceof EventPublishOperation) { // ????????????
                    EventPublishOperation eventPublishOperation = (EventPublishOperation) operation;
                    // ??????????????????
                    System.out.println(eventPublishOperation.getEventAddress());
                    // ??????
                    EventPublishOperation.EventEntry[] events = eventPublishOperation.getEvents();
                    for (EventPublishOperation.EventEntry event : events) {
                        // topic
                        System.out.println(event.getName());
                        // ??????????????????????????????
                        System.out.println(event.getSequence());
                        // ??????
                        BytesValue value = event.getContent();
                        switch (value.getType()) {
                            case TEXT:
                            case XML:
                            case JSON:
                                System.out.println(value.getBytes().toString());
                                break;
                            case INT64:
                            case TIMESTAMP:
                                System.out.println(BytesUtils.toLong(value.getBytes().toBytes()));
                                break;
                            default: // byte[], Bytes, IMG
                                System.out.println(value.getBytes());
                                break;
                        }
                    }
                } else if (operation instanceof ConsensusSettingsUpdateOperation) { // ??????????????????
                    ConsensusSettingsUpdateOperation consensusSettingsUpdateOperation = (ConsensusSettingsUpdateOperation) operation;
                    Property[] properties = consensusSettingsUpdateOperation.getProperties();
                    for (Property property : properties) {
                        System.out.println(property.getName());
                        System.out.println(property.getValue());
                    }
                } else if (operation instanceof LedgerInitOperation) { // ???????????????
                    LedgerInitOperation ledgerInitOperation = (LedgerInitOperation) operation;
                    // ????????????????????????
                    ledgerInitOperation.getInitSetting().getConsensusParticipants();
                    // ??????????????????
                    ledgerInitOperation.getInitSetting().getCryptoSetting();
                    // ???????????????
                    ledgerInitOperation.getInitSetting().getLedgerSeed();
                    // ...
                } else if (operation instanceof ParticipantRegisterOperation) { // ???????????????
                    ParticipantRegisterOperation participantRegisterOperation = (ParticipantRegisterOperation) operation;
                    // ???????????????
                    System.out.println(participantRegisterOperation.getParticipantID().getAddress());
                    // ???????????????
                    System.out.println(participantRegisterOperation.getParticipantID().getPubKey());
                    // ???????????????
                    System.out.println(participantRegisterOperation.getParticipantName());
                } else if (operation instanceof RolesConfigureOperation) { // ????????????
                    RolesConfigureOperation rolesConfigureOperation = (RolesConfigureOperation) operation;
                    // ????????????
                    RolesConfigureOperation.RolePrivilegeEntry[] roles = rolesConfigureOperation.getRoles();
                    for (RolesConfigureOperation.RolePrivilegeEntry role : roles) {
                        // ????????????
                        System.out.println(role.getRoleName());
                        // ?????????????????????
                        System.out.println(Arrays.toString(role.getEnableLedgerPermissions()));
                        // ?????????????????????
                        System.out.println(Arrays.toString(role.getDisableLedgerPermissions()));
                        // ?????????????????????
                        System.out.println(Arrays.toString(role.getEnableTransactionPermissions()));
                        // ?????????????????????
                        System.out.println(Arrays.toString(role.getDisableTransactionPermissions()));
                    }
                } else if (operation instanceof UserAuthorizeOperation) { // ????????????
                    UserAuthorizeOperation userAuthorizeOperation = (UserAuthorizeOperation) operation;
                    // ????????????
                    UserAuthorizeOperation.UserRolesEntry[] userRoles = userAuthorizeOperation.getUserRolesAuthorizations();
                    for (UserAuthorizeOperation.UserRolesEntry userRole : userRoles) {
                        // ????????????
                        System.out.println(Arrays.toString(userRole.getUserAddresses()));
                        // ?????????????????????
                        System.out.println(userRole.getPolicy());
                        // ?????????????????????
                        System.out.println(Arrays.toString(userRole.getAuthorizedRoles()));
                        // ???????????????????????????
                        System.out.println(Arrays.toString(userRole.getUnauthorizedRoles()));
                    }
                } else {
                    System.out.println("todo");
                }
            }
        }
    }

    /**
     * ????????????????????????????????????
     */
    @Test
    public void getAdditionalTransactionsByHash() {
        LedgerTransaction[] txs = blockchainService.getAdditionalTransactions(ledger, sampleHash, 0, 1);
        Assert.assertNull(txs);
    }

    /**
     * ????????????hash??????????????????
     */
    @Test
    public void getTransactionByContentHash() {
        LedgerTransaction tx = blockchainService.getTransactionByContentHash(ledger, sampleHash);
        Assert.assertNull(tx);
    }

    /**
     * ????????????hash??????????????????
     */
    @Test
    public void getTransactionStateByContentHash() {
        TransactionState state = blockchainService.getTransactionStateByContentHash(ledger, sampleHash);
        Assert.assertNull(state);
    }

    /**
     * ??????????????????????????????
     */
    @Test
    public void getUser() {
        UserInfo user = blockchainService.getUser(ledger, sampleUserAddress);
        if (null != user) {
            System.out.println(user.getAddress().toString());
        }
    }

    /**
     * ??????????????????????????????
     */
    @Test
    public void getDataAccount() {
        DataAccountInfo dataAccount = blockchainService.getDataAccount(ledger, sampleDataAccountAddress);
        if (null != dataAccount) {
            System.out.println(dataAccount.getAddress().toString());
        }
    }

    /**
     * ????????????????????????KV???????????????????????????????????????
     */
    @Test
    public void getDataEntriesByKey() {
        TypedKVEntry[] kvs = blockchainService.getDataEntries(ledger, sampleDataAccountAddress, sampleKey);
        for (TypedKVEntry kv : kvs) {
            System.out.println(kv.getKey() + ":" + kv.getVersion() + ":" + kv.getValue());
        }
    }

    /**
     * ?????????????????????????????????????????????KV??????
     */
    @Test
    public void getDataEntriesWithKeyAndVersion() {
        TypedKVEntry[] kvs = blockchainService.getDataEntries(ledger, sampleDataAccountAddress, new KVInfoVO(new KVDataVO[]{new KVDataVO(sampleKey, new long[]{0})}));
        for (TypedKVEntry kv : kvs) {
            System.out.println(kv.getKey() + ":" + kv.getVersion() + ":" + kv.getValue());
        }
    }

    /**
     * ??????????????????KV??????
     */
    @Test
    public void getDataEntriesTotalCount() {
        long count = blockchainService.getDataEntriesTotalCount(ledger, sampleDataAccountAddress);
        System.out.println(count);
    }

    /**
     * ??????????????????????????????KV??????
     */
    @Test
    public void getDataEntries() {
        TypedKVEntry[] kvs = blockchainService.getDataEntries(ledger, sampleDataAccountAddress, 0, 1);
        for (TypedKVEntry kv : kvs) {
            System.out.println(kv.getKey() + ":" + kv.getVersion() + ":" + kv.getValue());
        }
    }

    /**
     * ??????????????????
     */
    @Test
    public void getContract() {
        ContractInfo contract = blockchainService.getContract(ledger, sampleContractAddress);
        if (null != contract) {
            // ????????????
            System.out.println(contract.getAddress());
            // ????????????
            System.out.println(BytesUtils.toString(contract.getChainCode()));
            System.out.println(OnLineContractProcessor.getInstance().decompileEntranceClass(contract.getChainCode()));
        }
    }

    /**
     * ????????????????????????????????????????????????
     */
    @Test
    public void getSystemEvents() {
        Event[] events = blockchainService.getSystemEvents(ledger, sampleEvent, 0, 1);
        Assert.assertTrue(null == events || events.length == 0);
    }

    /**
     * ???????????????????????????
     */
    @Test
    public void getSystemEventNameTotalCount() {
        long count = blockchainService.getSystemEventNameTotalCount(ledger);
        Assert.assertEquals(0, count);
    }

    /**
     * ???????????????????????????
     */
    @Test
    public void getSystemEventNames() {
        String[] names = blockchainService.getSystemEventNames(ledger, 0, 1);
        Assert.assertEquals(0, names.length);
    }

    /**
     * ???????????????????????????????????????
     */
    @Test
    public void getLatestEvent() {
        Event event = blockchainService.getLatestSystemEvent(ledger, sampleEvent);
        Assert.assertNull(event);
    }

    /**
     * ??????????????????????????????????????????
     */
    @Test
    public void getSystemEventsTotalCount() {
        long count = blockchainService.getSystemEventsTotalCount(ledger, sampleEvent);
        Assert.assertEquals(0, count);
    }

    /**
     * ??????????????????????????????
     *
     * @return
     */
    @Test
    public void getUserEventAccounts() {
        BlockchainIdentity[] ids = blockchainService.getUserEventAccounts(ledger, 0, 1);
        System.out.println(ids.length);
    }

    /**
     * ????????????????????????
     */
    @Test
    public void getUserEventAccount() {
        BlockchainIdentity id = blockchainService.getUserEventAccount(ledger, sampleEventAddress);
        if (null != id) {
            System.out.println(id.getAddress().toString());
        }
    }

    /**
     * ??????????????????????????????
     */
    @Test
    public void getUserEventAccountTotalCount() {
        long count = blockchainService.getUserEventAccountTotalCount(ledger);
        System.out.println(count);
    }

    /**
     * ?????????????????????????????????????????????
     */
    @Test
    public void getUserEventNameTotalCount() {
        long count = blockchainService.getUserEventNameTotalCount(ledger, sampleEventAddress);
        System.out.println(count);
    }

    /**
     * ????????????????????????????????????????????????
     */
    @Test
    public void getUserEventNames() {
        String[] names = blockchainService.getUserEventNames(ledger, sampleEventAddress, 0, 1);
        for (String name : names) {
            System.out.println(name);
        }
    }

    /**
     * ????????????????????????????????????????????????????????????
     */
    @Test
    public void getLatestUserEvent() {
        Event event = blockchainService.getLatestUserEvent(ledger, sampleEventAddress, sampleEvent);
        if (null != event) {
            BytesValue content = event.getContent();
            switch (content.getType()) {
                case TEXT:
                case XML:
                case JSON:
                    System.out.println(event.getName() + ":" + event.getSequence() + ":" + content.getBytes().toUTF8String());
                    break;
                case INT64:
                case TIMESTAMP:
                    System.out.println(event.getName() + ":" + event.getSequence() + ":" + BytesUtils.toLong(content.getBytes().toBytes()));
                    break;
                default: // byte[], Bytes
                    System.out.println(event.getName() + ":" + event.getSequence() + ":" + content.getBytes().toBase58());
                    break;
            }
        }
    }

    /**
     * ????????????????????????????????????????????????????????????
     */
    @Test
    public void getUserEventsTotalCount() {
        long count = blockchainService.getUserEventsTotalCount(ledger, sampleEventAddress, sampleEvent);
        System.out.println(count);
    }

    /**
     * ????????????????????????????????????????????????????????????
     */
    @Test
    public void getUserEvents() {
        Event[] events = blockchainService.getUserEvents(ledger, sampleEventAddress, sampleEvent, 0, 1);
        for (Event event : events) {
            BytesValue content = event.getContent();
            switch (content.getType()) {
                case TEXT:
                case XML:
                case JSON:
                    System.out.println(event.getName() + ":" + event.getSequence() + ":" + content.getBytes().toUTF8String());
                    break;
                case INT64:
                case TIMESTAMP:
                    System.out.println(event.getName() + ":" + event.getSequence() + ":" + BytesUtils.toLong(content.getBytes().toBytes()));
                    break;
                default: // byte[], Bytes
                    System.out.println(event.getName() + ":" + event.getSequence() + ":" + content.getBytes().toBase58());
                    break;
            }
        }
    }

    /**
     * ????????????????????????
     */
    @Test
    public void getContractByAddressAndVersion() {
        ContractInfo contract = blockchainService.getContract(ledger, sampleContractAddress, sampleVersion);
        if (null != contract) {
            System.out.println(contract.getAddress().toString());
            System.out.println(contract.getChainCodeVersion());
            System.out.println(OnLineContractProcessor.getInstance().decompileEntranceClass(contract.getChainCode()));
        }
    }

    /**
     * ??????????????????
     */
    @Test
    public void getUsers() {
        BlockchainIdentity[] ids = blockchainService.getUsers(ledger, 0, 1);
        Assert.assertEquals(1, ids.length);
    }

    /**
     * ????????????????????????
     */
    @Test
    public void getDataAccounts() {
        BlockchainIdentity[] ids = blockchainService.getDataAccounts(ledger, 0, 1);
        System.out.println(ids.length);
    }

    /**
     * ????????????????????????
     */
    @Test
    public void getContractAccounts() {
        BlockchainIdentity[] ids = blockchainService.getContractAccounts(ledger, 0, 1);
        System.out.println(ids.length);
    }

    /**
     * ??????????????????????????????
     */
    @Test
    public void getRolePrivileges() {
        PrivilegeSet privilegeSet = blockchainService.getRolePrivileges(ledger, sampleRoleName);
        if (null != privilegeSet) {
            for (LedgerPermission ledgerpermission : privilegeSet.getLedgerPrivilege().getPrivilege()) {
                System.out.println(ledgerpermission);
            }
            for (TransactionPermission transactionPermission : privilegeSet.getTransactionPrivilege().getPrivilege()) {
                System.out.println(transactionPermission);
            }
        }
    }

    /**
     * ??????????????????????????????
     */
    @Test
    public void getUserPrivileges() {
        UserPrivilegeSet userPrivileges = blockchainService.getUserPrivileges(ledger, sampleUserAddress);
        if (null != userPrivileges) {
            for (String role : userPrivileges.getUserRole()) {
                System.out.println(role);
            }
            for (LedgerPermission ledgerpermission : userPrivileges.getLedgerPrivilegesBitset().getPrivilege()) {
                System.out.println(ledgerpermission);
            }
            for (TransactionPermission transactionPermission : userPrivileges.getTransactionPrivilegesBitset().getPrivilege()) {
                System.out.println(transactionPermission);
            }
        }
    }
}
