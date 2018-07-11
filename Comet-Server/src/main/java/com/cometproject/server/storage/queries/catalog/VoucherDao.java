package com.cometproject.server.storage.queries.catalog;

import com.cometproject.server.game.catalog.types.CatalogVoucher;
import com.cometproject.server.storage.SqlHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VoucherDao {
    public static CatalogVoucher findVoucherByCode(final String code) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        CatalogVoucher voucher = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM `catalog_vouchers` WHERE `code` = ? AND `limit_use` > 0", sqlConnection);

            preparedStatement.setString(1, code);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                voucher = new CatalogVoucher(resultSet.getInt("id"), CatalogVoucher.VoucherType.valueOf(resultSet.getString("type")),
                        resultSet.getString("data"), resultSet.getInt("created_by"), resultSet.getInt("created_at"),
                        resultSet.getString("claimed_by"), resultSet.getInt("claimed_at"), resultSet.getInt("limit_use"),
                        CatalogVoucher.VoucherStatus.valueOf(resultSet.getString("status")), resultSet.getString("code"));
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return voucher;
    }

    public static void claimVoucher(final int voucherId, final int playerId, final boolean claimed) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE `catalog_vouchers` SET `status` = ?, `claimed_by` = concat(?, claimed_by), `limit_use` = limit_use - 1, `claimed_at` = UNIX_TIMESTAMP() WHERE id = ?;", sqlConnection);

            preparedStatement.setString(1, claimed ? "CLAIMED" : "UNCLAIMED");
            preparedStatement.setString(2, playerId + ",");
            preparedStatement.setInt(3, voucherId);

            preparedStatement.execute();

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}