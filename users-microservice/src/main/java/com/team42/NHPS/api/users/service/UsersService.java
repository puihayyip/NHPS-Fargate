/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team42.NHPS.api.users.service;

import com.team42.NHPS.api.users.shared.UserDto;

import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService  extends UserDetailsService {
    public List<UserDto> getUsers();
    public UserDto createUser(UserDto userDto);
    public UserDto getUserByEmail(String email);
    public void deleteUser(String userId, String authorizationHeader);
//    public List<PatientsResponseModel> getUserAlbums(String jwt);
    public UserDto getUserByUserId(String userId);
}
