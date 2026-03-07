package team.four.pas.repositories;

import team.four.pas.exceptions.AppBaseException;

import java.util.List;

interface Repository<T> {
    List<T> getAll();

    T findById(String id) throws AppBaseException;
}
