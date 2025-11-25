package com.ecommerce.dream_shops.service.user;

import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.ecommerce.dream_shops.dto.UserDto;
import com.ecommerce.dream_shops.exceptions.AlreadyExistsException;
import com.ecommerce.dream_shops.exceptions.ResourceNotFoundExcception;
import com.ecommerce.dream_shops.model.User;
import com.ecommerce.dream_shops.repository.UserRepository;
import com.ecommerce.dream_shops.request.CreateUserRequest;
import com.ecommerce.dream_shops.request.UserUpdateRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

  
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundExcception("User not found"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
       return Optional.of(request)
       .filter((req)-> !userRepository.existsByEmail(request.getEmail()))
       .map(req->{
        User user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(request.getPassword())
        .build();

        return userRepository.save(user);
       }).orElseThrow(()-> new AlreadyExistsException("Ooops this user already exists" + request.getEmail() ));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
       return userRepository.findById(userId).map(existingUser->{
        existingUser.setFirstname(request.getFirstname());
        existingUser.setLastname(request.getLastname());
        return userRepository.save(existingUser);
       }).orElseThrow(()-> new ResourceNotFoundExcception("User not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository::delete, () -> {
            throw new ResourceNotFoundExcception("User not found");
        });
    }


    @Override
    public UserDto convertUserToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }

}
