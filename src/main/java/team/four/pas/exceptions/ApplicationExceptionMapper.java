package team.four.pas.exceptions;

import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;
import team.four.pas.exceptions.allocation.*;
import team.four.pas.exceptions.resource.ResourceDataException;
import team.four.pas.exceptions.resource.ResourceIdException;
import team.four.pas.exceptions.resource.ResourceNotFoundException;
import team.four.pas.exceptions.resource.ResourceStillAllocatedException;
import team.four.pas.exceptions.user.*;

public class ApplicationExceptionMapper {

    @ServerExceptionMapper(value = {
            UserNotFoundException.class,
            AllocationNotFoundException.class,
            ResourceNotFoundException.class
    })
    public RestResponse<String> mapNotFound(AppBaseException e) {
        return RestResponse.status(Response.Status.NOT_FOUND, e.getMessage());
    }

    @ServerExceptionMapper(value = {
            UserAlreadyExistsException.class,
            ResourceAlreadyAllocatedException.class,
            ResourceStillAllocatedException.class,
            AllocationNotActiveException.class
    })
    public RestResponse<String> mapConflict(AppBaseException e) {
        return RestResponse.status(Response.Status.CONFLICT, e.getMessage());
    }

    @ServerExceptionMapper(value = {
            AllocationIdException.class,
            ResourceIdException.class,
            ResourceDataException.class,
            UserIdException.class,
            UserDataException.class,
            UserTypeException.class
    })
    public RestResponse<String> mapBadRequest(AppBaseException e) {
        return RestResponse.status(Response.Status.BAD_REQUEST, e.getMessage());
    }

    @ServerExceptionMapper(value = {
            InactiveClientException.class
    })
    public RestResponse<String> mapForbidden(AppBaseException e) {
        return RestResponse.status(Response.Status.FORBIDDEN, e.getMessage());
    }

}
