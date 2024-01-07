package ru.clevertec.dao.requests;

public class RequestsSQL {

    public static final String GET_CLIENT_ALL = """
            SELECT id, client_name, family_name, sur_name, birth_day
            FROM client
            WHERE id != ?
            """;

    public static final String GET_CLIENT_ID = """
            SELECT id, client_name, family_name, sur_name, birth_day
            FROM client
            WHERE id = ?""";

    public static final String CREATE_CLIENT =
            "INSERT INTO public.client (client_name, family_name, sur_name, birth_day)" +
                    "VALUES ( ?, ?, ?, ?) RETURNING id;";

    public static final String UPDATE_CLIENT = """
            UPDATE public.client
            SET client_name = ?,
                family_name = ?,
                sur_name    = ?,
                birth_day   = ?
            WHERE id = ?;
            """;

    public static final String DELETE_ID = """
            DELETE FROM public.client
            WHERE id = ?
            """;
}