package team.four.pas.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import team.four.pas.controllers.DTOs.UserAddDTO;
import team.four.pas.controllers.DTOs.UserModDTO;
import team.four.pas.controllers.DTOs.mappers.UserToDTO;
import team.four.pas.services.UserService;
import team.four.pas.services.data.users.User;


@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class UserControllerImpl {
    private final UserService userService;
    private final UserToDTO userToDTO;

    @GET
    public Response getAll() {
        return Response
                .status(Response.Status.OK)
                .entity(userToDTO.toDataList(userService.getAll()))
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") String id) {
        return Response
                .status(Response.Status.OK)
                .entity(userToDTO.toDTO(userService.findById(id)))
                .build();
    }

    @GET
    @Path("/login/{login}")
    public Response findPersonByLogin(@PathParam("login")
                                      @NotNull(message = "login can't be null")
                                      @Pattern(regexp = "^[A-Z][A-Z][a-z]{1,18}[0-9]{0,5}$",
                                              message = "Wrong format of login")
                                      String login) {
        return Response
                .status(Response.Status.OK)
                .entity(userToDTO.toDTO(userService.findByLogin(login)))
                .build();
    }

    @GET
    @Path("/search/{login}")
    public Response searchByLogin(@PathParam("login") String login) {
        return Response
                .status(Response.Status.OK)
                .entity(userToDTO.toDataList(userService.findByMatchingLogin(login)))
                .build();
    }

    @PUT
    @Path("/{id}/activate")
    public Response activateUser(@PathParam("id") String id) {
        userService.activate(id);
        return Response
                .status(Response.Status.OK)
                .build();
    }

    @PUT
    @Path("/{id}/deactivate")
    public Response deactivateUser(@PathParam("id") String id) {
        userService.deactivate(id);
        return Response
                .status(Response.Status.OK)
                .build();
    }

    @POST
    public Response createUser(@Valid UserAddDTO addDTO) {
        User user = userToDTO.toData(addDTO);
        return Response
                .status(Response.Status.CREATED)
                .entity(userToDTO.toDTO(userService.add(user)))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response editUser(@PathParam("id") String id, @Valid UserModDTO modDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(userService.update(id, modDTO.surname()))
                .build();
    }
}