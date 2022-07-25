package com.foodtech.store.photo.controller;

import com.foodtech.store.auth.exceptions.AuthException;
import com.foodtech.store.auth.exceptions.NotAccessException;
import com.foodtech.store.base.api.request.SearchRequest;
import com.foodtech.store.base.api.response.OkResponse;
import com.foodtech.store.base.api.response.SearchResponse;
import com.foodtech.store.photo.api.request.PhotoRequest;
import com.foodtech.store.photo.api.response.PhotoResponse;
import com.foodtech.store.photo.exeception.PhotoExistException;
import com.foodtech.store.photo.exeception.PhotoNotExistException;
import com.foodtech.store.photo.mapping.PhotoMapping;
import com.foodtech.store.photo.routes.PhotoApiRoutes;
import com.foodtech.store.photo.service.PhotoApiService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(value = "Photo api")
public class PhotoApiController {
    private final PhotoApiService photoApiService;

    @PostMapping(PhotoApiRoutes.ROOT)
    @ApiOperation(value = "Create (for admin)",notes="use this when you need create new Photo")
    @ApiResponses(value = {
           @ApiResponse(code = 200,message = "Success"),
           @ApiResponse(code = 400,message = "Photo already exist"),
            @ApiResponse(code = 403, message = "no access rights")
    })
    public @ResponseBody OkResponse<PhotoResponse> create(
            @RequestParam MultipartFile file)
            throws IOException,  NotAccessException, AuthException {
        return OkResponse.of(PhotoMapping.getInstance().getResponse().convert(photoApiService.create(file)));
    }

    @GetMapping(PhotoApiRoutes.ROOT)
    @ApiOperation(value = "search Photo(for admin)",notes = "use this if you need find Photo by title/all photo")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 403, message = "no access rights")
    })
    public OkResponse<SearchResponse<PhotoResponse>> search(
            @ModelAttribute SearchRequest request
    ) throws NotAccessException, AuthException {
        return  OkResponse.of(PhotoMapping.getInstance().getSearch().convert(
                photoApiService.search(request)
        ));
    }

    @GetMapping(PhotoApiRoutes.BY_ID)
    @ApiOperation(value = "find Photo by id",notes = "use this if you need full information by Photo")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 404,message = "Photo not found"),
    })
    public OkResponse<PhotoResponse> byId( @ApiParam(value = "Photo id")@PathVariable ObjectId id) throws ChangeSetPersister.NotFoundException {
    return  OkResponse.of(PhotoMapping.getInstance().getResponse().convert(
            photoApiService.findByID(id).orElseThrow(ChangeSetPersister.NotFoundException::new)
    ));
    }


    @DeleteMapping(PhotoApiRoutes.BY_ID)
    @ApiOperation(value = "delete Photo (for admin)",notes = "use this if you need delete Photo")
    @ApiResponses(value={
            @ApiResponse(code = 200,message = "Success"),
            @ApiResponse(code = 400, message = "Photo ID invalid"),
            @ApiResponse(code = 403, message = "no access rights")
    })
    public OkResponse<String> deleteById(@ApiParam(value = "Photo id") @PathVariable ObjectId id)
        throws PhotoNotExistException, NotAccessException, AuthException{
        photoApiService.delete(id);
        return OkResponse.of(HttpStatus.OK.toString());
    }
}
