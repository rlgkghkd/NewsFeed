package com.example.newsFeed.relation.entity;

import com.example.newsFeed.users.entity.User;
import com.example.newsFeed.entity.Base;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "userT")
@NoArgsConstructor
@AllArgsConstructor
public class UserRelation extends Base {
    @ManyToMany
    private List<User> follower;
    @ManyToMany
    private List<User> following;


}
