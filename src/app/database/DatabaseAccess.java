package app.database;

import CustomExceptions.NoUserFoundException;
import CustomExceptions.VoteAlreadyCastedException;
import app.models.Admin;
import app.models.PartyCandidate;
import app.models.Voter;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {

    public static Voter getVoterByCredentials(String cnic, String pin) throws SQLException, NoUserFoundException, VoteAlreadyCastedException {
        try {
            Connection conn = DatabaseConnector.getConnection();
            String voteCastedSqlQuery = """
                    SELECT *
                    FROM vote
                    WHERE vote.voter_cnic = ?
                    """;
            PreparedStatement preparedVoteCastedSqlQuery = conn.prepareStatement(voteCastedSqlQuery);
            preparedVoteCastedSqlQuery.setString(1, cnic);

            ResultSet voteCastedSqlQueryResult = preparedVoteCastedSqlQuery.executeQuery();

            if (voteCastedSqlQueryResult.next()) {
                throw new VoteAlreadyCastedException("You already casted your vote");
            }

            String sqlQuery = """
                    SELECT *
                    FROM voters
                    WHERE voters.cnic = ? AND voters.pin = ?
                    """;
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
            preparedStatement.setString(1, cnic);
            preparedStatement.setString(2, pin);

            ResultSet result = preparedStatement.executeQuery();

            String voterName = "";
            String voterCnic = "";
            String voterPin = "";
            int voterAge = 0;
            Voter voter = new Voter(voterName, voterCnic, voterPin, voterAge);

            if (result.next()) {
                voterName = result.getString("name");
                voterCnic = result.getString("cnic");
                voterPin = result.getString("pin");
                voterAge = result.getInt("age");

                voter.setName(voterName);
                voter.setCnic(voterCnic);
                voter.setPin(voterPin);
                voter.setAge(voterAge);

            } else {
                throw new NoUserFoundException("No user with these credentials was found");
            }
            result.close();
            preparedStatement.close();
            conn.close();

            return voter;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Admin getAdminByCredentials(String adminID, String pin) throws SQLException, NoUserFoundException, VoteAlreadyCastedException {
        try {
            Connection conn = DatabaseConnector.getConnection();
            String sqlQuery = """
                    SELECT *
                    FROM admins
                    WHERE admins.adminID = ? AND admins.pin = ?
                    """;
            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
            preparedStatement.setString(1, adminID);
            preparedStatement.setString(2, pin);

            ResultSet result = preparedStatement.executeQuery();

            String adminName = "";
            String adminId = "";
            String adminPin = "";

            Admin admin = new Admin(adminName, adminId, adminPin);

            if (result.next()) {
                adminName = result.getString("adminName");
                adminId = result.getString("adminID");
                adminPin = result.getString("pin");

                admin.setAdminName(adminName);
                admin.setAdminID(adminId);
                admin.setPin(adminPin);

            } else {
                throw new NoUserFoundException("No user with these credentials was found");
            }
            result.close();
            preparedStatement.close();
            conn.close();

            return admin;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Voter getVoterByCnic(String voterCnic) throws  SQLException, NoUserFoundException {
        try {
            Connection conn = DatabaseConnector.getConnection();

            String sqlQuery = """
                    SELECT *
                    FROM voters
                    WHERE voters.cnic = ?;
                    """;

            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
            preparedStatement.setString(1, voterCnic);

            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                String name = result.getString("name");
                String cnic = result.getString("cnic");
                int age = result.getInt("age");
                String pin = result.getString("pin");

                return new Voter(name, cnic, pin, age);
            } else {
                throw new NoUserFoundException("No record against this CNIC was found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<PartyCandidate> getAllCandidates() throws SQLException {
        try {
            Connection conn = DatabaseConnector.getConnection();
            Statement statement = conn.createStatement();
            String sqlQuery = """
                    SELECT voters.cnic, voters.name, parties.logo, parties.name as party_name
                    FROM candidates
                    JOIN parties ON candidates.party = parties.id
                    JOIN voters ON candidates.cnic = voters.cnic;""";

            ResultSet result = statement.executeQuery(sqlQuery);

            List <PartyCandidate> allCandidates = new ArrayList<>();

            while (result.next()) {
                PartyCandidate partyCandidate = new PartyCandidate(
                        result.getString("cnic"),
                        result.getString("name"),
                        result.getString("logo"),
                        result.getString("party_name")
                );
                allCandidates.add(partyCandidate);
            }

            return allCandidates;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String setVote(String candidateCnic, String voterCnic) throws SQLException {
        try {
            Connection conn = DatabaseConnector.getConnection();
            String sqlQuery = """
                    INSERT INTO vote
                    VALUES (?, ?, ?);
                    """;

            String timeStamp = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(new java.util.Date());

            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
            preparedStatement.setString(1, candidateCnic);
            preparedStatement.setString(2, voterCnic);
            preparedStatement.setString(3, timeStamp);

            preparedStatement.executeUpdate();
            return "Your vote was casted successfully!";

        } catch (SQLException e) {
            if(e.getSQLState().equals("23000")) {
                return "You already casted your vote";
            }
            e.printStackTrace();
        }
        return null;
    }
    public static int getVotesCasted() throws SQLException {
        String sqlQuery = "SELECT COUNT(*) FROM vote";
        try {
            Connection conn = DatabaseConnector.getConnection();
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);

            if (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getVotesRemaining() throws SQLException {
        String sqlQuery = """
               SELECT COUNT(*)
               FROM voters
               WHERE voters.cnic NOT IN (
                    SELECT vote.voter_cnic FROM vote
                    )
                """;
        try {
            Connection conn = DatabaseConnector.getConnection();
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);

            if (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<PartyCandidate> getTopCandidates() throws SQLException {
        String sqlQuery = """
                SELECT candidates.cnic, voters.name, parties.name AS party_name, parties.logo, COUNT(vote.voter_cnic) AS votes
                FROM vote
                JOIN candidates ON vote.candidate_cnic = candidates.cnic
                JOIN voters ON candidates.cnic = voters.cnic
                JOIN parties ON candidates.party = parties.id
                GROUP BY candidates.cnic, voters.name, parties.name, parties.logo
                ORDER BY COUNT(vote.voter_cnic) DESC
                """;
        List<PartyCandidate> topCandidates = new ArrayList<>();
        try {
            Connection conn = DatabaseConnector.getConnection();
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);

            while (result.next()) {
                PartyCandidate candidate = new PartyCandidate(
                        result.getString("cnic"),
                        result.getString("name"),
                        result.getString("logo"),
                        result.getString("party_name")
                );
                candidate.setVotes(result.getInt("votes"));
                topCandidates.add(candidate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return topCandidates;
    }

    public static List<String> getAllParties() throws SQLException {
        List<String> parties = new ArrayList<>();
        String sqlQuery = "SELECT id, name FROM parties";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sqlQuery);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                String name = result.getString("name");
                parties.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return parties;
    }

    public static String addCandidate(String candidateCnic, String partyName) throws SQLException {
        try {
            Connection conn = DatabaseConnector.getConnection();
            String candidateExistsSqlQuery = """
                    SELECT parties.name
                    FROM parties
                    JOIN candidates ON candidates.party = parties.id
                    WHERE candidates.cnic = ?;
                    """;

            PreparedStatement candidateExistsStatement = conn.prepareStatement(candidateExistsSqlQuery);
            candidateExistsStatement.setString(1, candidateCnic);
            ResultSet candidateExists = candidateExistsStatement.executeQuery();

            if (candidateExists.next()) {
                return "Candidate is already part of " + candidateExists.getString("name");
            }

            String preprocessingSqlQuery = """
                    SELECT parties.id
                    FROM parties
                    WHERE parties.name = ?;
                    """;

            PreparedStatement preprocessingPreparedStatement = conn.prepareStatement(preprocessingSqlQuery);
            preprocessingPreparedStatement.setString(1, partyName);
            ResultSet result = preprocessingPreparedStatement.executeQuery();

            if (!result.next()) {
                return "No party with this name was found";
            }

            int selectedPartyID = result.getInt("id");

            System.out.println(selectedPartyID);

            String sqlQuery = """
                    INSERT INTO candidates
                    VALUES (?, ?);
                    """;

            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
            preparedStatement.setString(1, candidateCnic);
            preparedStatement.setInt(2, selectedPartyID);

            preparedStatement.executeUpdate();
            return "Candidate added successfully!";

        } catch (SQLException e) {
            if(e.getSQLState().equals("23000")) {
                return "Candidate already in a party";
            }
            e.printStackTrace();
        }
        return null;
    }

    public static String removeCandidate(String candidateCnic) {
        try {
            Connection conn = DatabaseConnector.getConnection();

            String getCandidateSqlQuery = """
                    SELECT *
                    FROM candidates
                    WHERE candidates.cnic = ?;
                    """;

            PreparedStatement getCandidatePreparedStatement = conn.prepareStatement(getCandidateSqlQuery);
            getCandidatePreparedStatement.setString(1, candidateCnic);
            ResultSet resultSet = getCandidatePreparedStatement.executeQuery();

            if (!resultSet.next()) {
                return "No candidate with this CNIC exists";
            }

            String sqlQuery = """
                    DELETE FROM candidates
                    WHERE candidates.cnic = ?;
                    """;

            PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
            preparedStatement.setString(1, candidateCnic);

            preparedStatement.executeUpdate();
            return "Candidate was deleted successfully!";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
