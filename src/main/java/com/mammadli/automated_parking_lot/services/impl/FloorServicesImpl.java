package com.mammadli.automated_parking_lot.services.impl;

import com.mammadli.automated_parking_lot.db.dto.FloorDto;
import com.mammadli.automated_parking_lot.db.entity.Floor;
import com.mammadli.automated_parking_lot.db.entity.ParkingLot;
import com.mammadli.automated_parking_lot.db.repository.FloorRepository;
import com.mammadli.automated_parking_lot.db.repository.ParkingLotRepository;
import com.mammadli.automated_parking_lot.mapper.FloorMapper;
import com.mammadli.automated_parking_lot.services.FloorServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FloorServicesImpl implements FloorServices {

    private final FloorRepository floorRepository;
    private final ParkingLotRepository parkingLotRepository;

    @Override
    public Floor create(FloorDto floorDto) {

        Optional<ParkingLot> parkingLot = parkingLotRepository.findById(floorDto.getParkingLotId());
        if (parkingLot.isEmpty()) {
            return null;
        }
        Floor newFloor = FloorMapper.INSTANCE.toFloor(floorDto);
        newFloor.setParkingLot(parkingLot.get());
        return floorRepository.save(newFloor);
    }


    @Override
    public Void delete(String id) {

        Optional<Floor> result = floorRepository.findById(id);
        result.ifPresent(floor -> floorRepository.deleteById(floor.getId()));
        return null;
    }

    @Override
    public List<FloorDto> getFloors(String parkingLotId) {
        List<Floor> floorList = floorRepository.findAllByParkingLotId(parkingLotId);
        if (!floorList.isEmpty()) {
            return floorList.stream()
                    .map(FloorMapper.INSTANCE::toFloorDto)
                    .collect(Collectors.toList());

        }
        return null;
    }
}
