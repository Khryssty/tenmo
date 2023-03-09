package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    /* TODO: Christy : need update
    */
    @Override
    public List<Transfer> findAll(int id) {
        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount " +
                "FROM transfer t " +
                "JOIN account a ON t.account_to = a.account_id OR t.account_from = a.account_id " +
                "JOIN tenmo_user tu ON a.user_id = tu.user_id WHERE tu.user_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        while(rowSet.next()) {
            transfers.add(mapRowToTransfer(rowSet));
        }
        return transfers;
    }

    @Override
    public Transfer getTransferById(int transfer_id) {
        Transfer transfer = new Transfer();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer WHERE transfer_id = ?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transfer_id);
        if (result.next()) {
            transfer = mapRowToTransfer(result);
        }

        return transfer;
    }

    /*
    *
    */
    @Override
    public Transfer sendTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer " +
                "(transfer_status_id, transfer_type_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransfer_status_id(), transfer.getTransfer_type_id(), transfer.getAccount_from(), transfer.getAccount_to(), transfer.getAmount());
        return getTransferById(transferId);
    }

    /*
    * TODO: Should be deleted later. Request transfer uses the sendTransfer method
    */
    @Override
    public Transfer requestTransfer(Transfer transfer, int user_id) {
        String sql = "INSERT INTO transfer(" +
                "transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransfer_type_id(), transfer.getTransfer_status_id(), transfer.getAccount_from(), transfer.getAccount_to(), transfer.getAmount());
        return getTransferById(transferId);

    }

    @Override
    public List<Transfer> viewPendingTransfers(int user_id) {
        List<Transfer> pendingTransfers = new ArrayList<>();
        String sql =  "SELECT t.transfer_id, t.transfer_type_id , ts.status_desc, , t.account_from, t.account_to, t.amount " +
                    "FROM transfer t " +
                    "JOIN transfer_status ts ON t.transfer_status_id = ts.transfer_status_id" +
                    "WHERE transfer_type_desc = 'PENDING' AND t.account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, Transfer.class, user_id);
        while (results.next()) {
            pendingTransfers.add(mapRowToTransfer(results));
        }
        return pendingTransfers;
    }

    @Override
    public Transfer approveTransfer(Transfer transfer, int transfer_id) {
        String sql = "UPDATE transfer " +
                "SET transfer_status_id=? WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, 2, transfer_id);

        return getTransferById(transfer_id);
    }

    @Override
    public Transfer rejectTransfer(Transfer transfer, int transfer_id) {
        String sql = "UPDATE transfer " +
                "SET transfer_status_id=? WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, 3, transfer_id);

        return getTransferById(transfer_id);
    }



    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();

        transfer.setTransfer_id(rowSet.getInt("transfer_id"));
        transfer.setTransfer_type_id(rowSet.getInt("transfer_type_id"));
        transfer.setTransfer_status_id(rowSet.getInt("transfer_status_id"));
        transfer.setAccount_from(rowSet.getInt("account_from"));
        transfer.setAccount_to(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));

        return transfer;
    }
}
