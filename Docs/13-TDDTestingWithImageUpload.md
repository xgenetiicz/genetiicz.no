# 13 TDD- Image Upload Testing

## What I did implement:

Before actually implementing the actual whole feature for uploading a image, i decided to write the 
test first. This test went through as TDD - Test Driven Development where i wrote the test until it succeded.
I liked this very much, because it make me think crucial about how i should approach this type of feature - but also
how i should actually implement it, to avoid unnecessary Nullpointers and also how the architecture should operate
behind this in terms of what use.

- So the idea is to store them on my raspberry pi, on a direct path since I don't want to pay for 
a Cloud provider.
- So the idea was to set a uploadPath with a reference variable:

````java
import java.nio.file.Paths;

Paths uploadImage = Paths.get("upload/projects") //get the URI - Uniform Resource Identificator
```` 
````java
//TODO: the URI path should have "upload/projects/id" since the image should be related to the actual project.
//This is why i write documentation - it helps me reflect on the changes and understand why.
````

### Let's keep going:
- So the test went through as the passwordTest.
First I needed to ``arrange`` the test

`````java
import com.example.genetiicz.Entity.ProjectEntity;

ProjectEntity projectImage = new ProjectEntity();
projectImage.setImagePath("upload/projects/test-image.jpg");

/*
        I would need to mock the file to have something
        to test with right, and MultipartFile is an interface that have
        the actual method for uploading files to an web-application
        
        so for this test: I used the MockMultipartFile ofc ;) 
 */

MockMultipartFile mockFile = new MockMultipartFile)(
        "file", //the name of the file
        "test-image.jpg", //the actual file
        "image/jpeg", //type of content
        "fake image content".getBytes()
        );
`````

### After the arrangement:
- I needed to assert the test, and failed couple of times.
- I thought of using when, like a trigger to see when im trying to upload
that the file is actually finding the project by projectId and userId.

````java
//The final result went like this:
when(projectRepository.findProjectByProjectIdAndUserEntity_UserId(123L,1L)) //so projectId and userId
        .thenReturn(Optional.of(projectImage)); //Then the desired return should be 
//optional, since user could have no projects also

String result = projectService.uploadProjectImage(123L,1L,mockFile) //the actual file related to the projects user.
assertTrue(result.startsWith("uploads/projects"));

System.out.println(result); //wanted to see the result that is why i stored it into a String variable.
````

### Importance of TDD development of my own reflection
- I feel a lot safer now implementing features than before.
- I reconsider choices and reflect more before implementing the specific feature
- It helps me define a business logic, and thinking more critically on how to intepret a problem and actually solving it.
- Avoid huge mistakes in the future.
