package team.four.pas.repositories;

import team.four.pas.exceptions.resource.ResourceException;
import team.four.pas.exceptions.user.UserException;

import java.util.List;

interface Repository<T> {
    List<T> getAll();

    T findById(String id) throws ResourceException, UserException;
}
