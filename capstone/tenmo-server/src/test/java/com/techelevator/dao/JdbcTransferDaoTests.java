package com.techelevator.dao;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests{
    private static final Transfer  REQUEST_1 = new Transfer(3001, 1, 1, 2001, 2002, BigDecimal.valueOf(11.11));
    private Transfer REQUEST_2 = new Transfer(3002, 1, 1, 2002, 2003, BigDecimal.valueOf(22.22));
    private Transfer SEND_1 = new Transfer(3003, 2, 2, 2003, 2001, BigDecimal.valueOf(33.33));
    private Transfer SEND_2 = new Transfer(3004, 2, 2, 2002, 2001, BigDecimal.valueOf(44.44));

    private JdbcTransferDao sut;
    private Transfer testTransferSend;
    private Transfer testTransferRequest;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
        testTransferSend = new Transfer(0, 2, 2, 2001, 2003, BigDecimal.valueOf(100.55));
        testTransferRequest = new Transfer(0, 1, 1, 2002, 2003, BigDecimal.valueOf(200.55));

    }

    @Test
    public void findAllTransactionsByUserIdReturnsAll() {
        List<Transfer> transfers = sut.findAll(1001);
        Assert.assertEquals(3, transfers.size());

        assertTransfersMatch(REQUEST_1, transfers.get(0));
        assertTransfersMatch(SEND_1, transfers.get(1));
        assertTransfersMatch(SEND_2, transfers.get(2));
    }

    @Test
    public void findAllTransactionsReturnsZeroForInvalidUserId() {
        List<Transfer> transfers = sut.findAll(99);
        Assert.assertEquals(0, transfers.size());
    }


    @Test
    public void returnsCorrectTransferByTransferId() {
        Transfer transfer = sut.getTransferById(3001);
        assertTransfersMatch(REQUEST_1, transfer);
    }

    @Test
    public void returnsNullForInvalidTransferId() {
        Transfer transfer = sut.getTransferById(9999);
        Assert.assertNull(transfer);
    }

    @Test
    public void returnsValidSendTransfer() {
        Transfer createdSendTransfer = sut.sendTransfer(testTransferSend);

        Integer newId = createdSendTransfer.getTransfer_id();
        Assert.assertTrue(newId > 0);

        testTransferSend.setTransfer_id(newId);
        assertTransfersMatch(testTransferSend, createdSendTransfer);
    }


    @Test
    public void returnsValidRequestTransfer() {
        Transfer createdRequestTransfer = sut.sendTransfer(testTransferRequest);
        Integer newId = createdRequestTransfer.getTransfer_id();
        Assert.assertTrue(newId > 0);

        testTransferRequest.setTransfer_id(newId);
        assertTransfersMatch(testTransferRequest, createdRequestTransfer);
    }

    @Test
    public void returnsValidPendingTransfers(){
        List<Transfer> transfers = sut.viewPendingTransfers(2002);
        Assert.assertEquals(1,transfers.size());

        assertTransfersMatch(REQUEST_2, transfers.get(0));

        transfers = sut.viewPendingTransfers(2001);
        Assert.assertEquals(1, transfers.size());
        assertTransfersMatch(REQUEST_1, transfers.get(0));
    }

    @Test
    public void approveTransferShouldReturnStatusIdOfTwo() {
        Transfer transfer = sut.approveTransfer(REQUEST_2, 3002);
        Assert.assertTrue(transfer.getTransfer_status_id() == 2);

        assertTransfersMatch(transfer, sut.getTransferById(3002));
    }

    @Test
    public void rejectTransferShouldReturnStatusIdOfThree() {
        Transfer transfer = sut.rejectTransfer(REQUEST_1, 3001);
        Assert.assertTrue(transfer.getTransfer_status_id() == 3);

        assertTransfersMatch(transfer, sut.getTransferById(3001));

    }

    private void assertTransfersMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getTransfer_id(), actual.getTransfer_id());
        Assert.assertEquals(expected.getTransfer_type_id(), actual.getTransfer_type_id());
        Assert.assertEquals(expected.getTransfer_status_id(), actual.getTransfer_status_id());
        Assert.assertEquals(expected.getAccount_from(), actual.getAccount_from());
        Assert.assertEquals(expected.getAccount_to(), actual.getAccount_to());
        Assert.assertEquals(expected.getAmount(), actual.getAmount());
    }


}
