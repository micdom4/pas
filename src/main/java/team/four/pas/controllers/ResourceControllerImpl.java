package team.four.pas.controllers;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import team.four.pas.controllers.DTOs.ResourceAddDTO;
import team.four.pas.controllers.DTOs.mappers.ResourceToDTO;
import team.four.pas.services.ResourceService;

@Path("/resources")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class ResourceControllerImpl {
    private final ResourceService resourceService;
    private final ResourceToDTO resourceToDTO;

    @GET
    public Response getAll() {
        return Response
                .status(Response.Status.OK)
                .entity(resourceToDTO.toDataList(resourceService.getAll()))
                .build();
    }

    @GET
    @Path("/{id}")
    public Response getResource(@PathParam("id") String id) {
        return Response
                .status(Response.Status.OK)
                .entity(resourceToDTO.toDTO(resourceService.findById(id)))
                .build();
    }

    @POST
    public Response createVM(@Valid ResourceAddDTO vmDto) {
        return Response
                .status(Response.Status.CREATED)
                .entity(resourceToDTO.dtoFromVM(resourceService.addVM(resourceToDTO.vmFromAddDTO(vmDto))))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateVM(@PathParam("id") String id, @Valid ResourceAddDTO vmDto) {
        return Response
                .status(Response.Status.OK)
                .entity(resourceToDTO.toDTO(resourceService.updateVM(id, vmDto.cpuNumber(), vmDto.ramGiB(), vmDto.storageGiB())))
                .build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteVM(@PathParam("id") String id) {
        resourceService.deleteVM(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}