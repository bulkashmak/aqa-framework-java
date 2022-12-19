package uz.gateway.dto.users.admin.users.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGetUsers {

    @JsonProperty("data")
    private Data data;
    @JsonProperty("errorMessage")
    private Object errorMessage;
    @JsonProperty("timestamp")
    private Long timestamp;

    @lombok.Data
    public static class Data {

        @JsonProperty("content")
        private List<Content> content;
        @JsonProperty("pageable")
        private Pageable pageable;
        @JsonProperty("totalPages")
        private Integer totalPages;
        @JsonProperty("totalElements")
        private Integer totalElements;
        @JsonProperty("last")
        private Boolean last;
        @JsonProperty("size")
        private Integer size;
        @JsonProperty("number")
        private Integer number;
        @JsonProperty("sort")
        private SortData sort;
        @JsonProperty("numberOfElements")
        private Integer numberOfElements;
        @JsonProperty("first")
        private Boolean first;
        @JsonProperty("empty")
        private Boolean empty;

        @lombok.Data
        public static class Content {

            @JsonProperty("id")
            private Integer id;
            @JsonProperty("firstName")
            private String firstName;
            @JsonProperty("lastName")
            private Object lastName;
            @JsonProperty("fullName")
            private Object fullName;
            @JsonProperty("emailAddress")
            private Object emailAddress;
            @JsonProperty("userId")
            private String userId;
            @JsonProperty("encPassword")
            private String encPassword;
            @JsonProperty("roleId")
            private Object roleId;
            @JsonProperty("tin")
            private Object tin;
            @JsonProperty("phoneNumber")
            private String phoneNumber;
            @JsonProperty("gender")
            private Object gender;
            @JsonProperty("personalIdentificationNumber")
            private Object personalIdentificationNumber;
            @JsonProperty("placeOfBirth")
            private Object placeOfBirth;
            @JsonProperty("passportSeries")
            private Object passportSeries;
            @JsonProperty("passportNumber")
            private Object passportNumber;
            @JsonProperty("state")
            private String state;
            @JsonProperty("locked")
            private Boolean locked;
        }

        @lombok.Data
        public static class Pageable {

            @JsonProperty("sort")
            private Sort sort;
            @JsonProperty("offset")
            private Integer offset;
            @JsonProperty("pageNumber")
            private Integer pageNumber;
            @JsonProperty("pageSize")
            private Integer pageSize;
            @JsonProperty("paged")
            private Boolean paged;
            @JsonProperty("unpaged")
            private Boolean unpaged;

            @lombok.Data
            public static class Sort {

                @JsonProperty("empty")
                private Boolean empty;
                @JsonProperty("sorted")
                private Boolean sorted;
                @JsonProperty("unsorted")
                private Boolean unsorted;
            }
        }

        @lombok.Data
        public static class SortData {

            @JsonProperty("empty")
            private Boolean empty;
            @JsonProperty("sorted")
            private Boolean sorted;
            @JsonProperty("unsorted")
            private Boolean unsorted;
        }
    }
}
