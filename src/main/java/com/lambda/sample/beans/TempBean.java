package com.lambda.sample.beans;



import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "temp")
@Getter
@Setter
public class TempBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String first_name;
    private String last_name;
    private String email;
    private String mobile_no;

}
