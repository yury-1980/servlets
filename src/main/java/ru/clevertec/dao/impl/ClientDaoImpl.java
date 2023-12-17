package ru.clevertec.dao.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.dao.ClientDao;
import ru.clevertec.dao.ConnectionPoolManager;
import ru.clevertec.dao.requests.RequestsSQL;
import ru.clevertec.entity.Client;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
public class ClientDaoImpl implements ClientDao {

    private ConnectionPoolManager connectionPoolManager;

    @Override
    public Optional<Client> findById(long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Client client = null;

        try {
            connection = connectionPoolManager.getConnection();
            preparedStatement = connection.prepareStatement(RequestsSQL.GET_CLIENT_ID);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            resultSet.next();

            long clientId = resultSet.getLong(1);
            String clientName = resultSet.getString(2);
            String familyName = resultSet.getString(3);
            String surName = resultSet.getString(4);
            LocalDate birthDay = resultSet.getDate(5).toLocalDate();

            client = new Client(clientId, clientName, familyName, surName, birthDay);

        } catch (SQLException e) {
            log.info("Failed to select client!" + e);
        } finally {
            closes(resultSet, connection, preparedStatement);
        }

        return Optional.ofNullable(client);
    }

    @Override
    public List<Client> findByAll() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Client> clientList = new ArrayList<>();
        Client client;

        try {
            connection = connectionPoolManager.getConnection();
            preparedStatement = connection.prepareStatement(RequestsSQL.GET_CLIENT_ALL);
            preparedStatement.setLong(1, 0);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                long clientId = resultSet.getLong(1);
                String clientName = resultSet.getString(2);
                String familyName = resultSet.getString(3);
                String surName = resultSet.getString(4);
                LocalDate birthDay = resultSet.getDate(5).toLocalDate();

                client = new Client(clientId, clientName, familyName, surName, birthDay);
                clientList.add(client);
            }

        } catch (SQLException e) {
            log.info("Connection not found." + e);
        } finally {
            closes(resultSet, connection, preparedStatement);
        }

        return clientList;
    }

    @Override
    public Client create(Client client) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int lastId = 0;

        try {
            connection = connectionPoolManager.getConnection();
            preparedStatement = connection.prepareStatement(RequestsSQL.CREATE_CLIENT);
            preparedStatement.setString(1, client.getClientName());
            preparedStatement.setString(2, client.getFamilyName());
            preparedStatement.setString(3, client.getSurName());
            preparedStatement.setDate(4, Date.valueOf(client.getBirthDay()));

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                lastId = rs.getInt(1);
            }
            connection.commit();
            client.setId(lastId);

        } catch (SQLException e) {
            log.info("Connection not found." + e);
        } finally {
            try {

                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connectionPoolManager.releaseConnection(connection);
                }
            } catch (SQLException e) {
                log.info("Not successful!");
            }
        }
        return client;
    }

    @Override
    public Client update(long id, Client client) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connectionPoolManager.getConnection();
            preparedStatement = connection.prepareStatement(RequestsSQL.UPDATE_CLIENT);
            preparedStatement.setString(1, client.getClientName());
            preparedStatement.setString(2, client.getFamilyName());
            preparedStatement.setString(3, client.getSurName());
            preparedStatement.setDate(4, Date.valueOf(client.getBirthDay()));
            preparedStatement.setLong(5, id);
            preparedStatement.executeUpdate();
            connection.commit();

            client.setId(id);

        } catch (SQLException e) {
            log.info("Connection not found." + e);
        } finally {
            try {

                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connectionPoolManager.releaseConnection(connection);
                }
            } catch (SQLException e) {
                log.info("Not successful!");
            }
        }
        return client;
    }

    @Override
    public void delete(long id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connectionPoolManager.getConnection();
            preparedStatement = connection.prepareStatement(RequestsSQL.DELETE_ID);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();

        } catch (SQLException e) {
            log.info("Delete failed!" + e);
        } finally {
            try {

                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connectionPoolManager.releaseConnection(connection);
                }
            } catch (SQLException e) {
                log.info("Not successful!");
            }
        }

    }

    private void closes(ResultSet resultSet, Connection connection, PreparedStatement preparedStatement) {

        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connectionPoolManager.releaseConnection(connection);
            }
        } catch (SQLException e) {
            System.out.println("Not successful!");
        }
    }
}
