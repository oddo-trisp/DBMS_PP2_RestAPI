package gr.uoa.di.dbm.restapi.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Document(collection="citizen")
@TypeAlias("citizen")
public class Citizen implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String citizenId;

    @Field("name")
    private String name;

    @Field("birthday")
    private String birthday;

    @Field("occupation")
    private String occupation;

    @Field("phone")
    private String phone;

    @Field("email")
    private String email;

    @Field("votes")
    private Integer votes;

    @Field("upvotedRequests")
    private List<UpvotedRequest> upvotedRequests;

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    public void increaseVotes(){
        this.votes++;
    }

    public List<UpvotedRequest> getUpvotedRequests() {
        return upvotedRequests;
    }

    public void setUpvotedRequests(List<UpvotedRequest> upvotedRequests) {
        this.upvotedRequests = upvotedRequests;
    }

    public void addUpvotedRequest(UpvotedRequest serviceRequest){
        if(upvotedRequests == null)
            upvotedRequests = new ArrayList<>();
        upvotedRequests.add(serviceRequest);
    }

    public Citizen getVoterFromCitizen(){
        Citizen citizen = new Citizen();
        citizen.citizenId = citizenId;
        citizen.phone = phone;
        citizen.name = name;
        return citizen;
    }
}
