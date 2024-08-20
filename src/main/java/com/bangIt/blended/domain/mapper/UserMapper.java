package com.bangIt.blended.domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bangIt.blended.domain.dto.user.admin.GetActivityLogDTO;
import com.bangIt.blended.domain.dto.user.admin.GetReservationDTO;
import com.bangIt.blended.domain.dto.user.admin.getUserDTO;

@Mapper
public interface UserMapper {

	List<getUserDTO> getAllUsers();

	List<getUserDTO> searchUsers(String username);

	List<GetReservationDTO> getUserReservations(Long userId);

	List<GetActivityLogDTO> getUserActivityLogs(Long userId);

}
