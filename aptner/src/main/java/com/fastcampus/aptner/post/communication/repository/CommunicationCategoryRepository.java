package com.fastcampus.aptner.post.communication.repository;

import com.fastcampus.aptner.apartment.domain.Apartment;
import com.fastcampus.aptner.post.communication.domain.CommunicationCategory;
import com.fastcampus.aptner.post.communication.domain.CommunicationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunicationCategoryRepository extends JpaRepository<CommunicationCategory,Long> {
    List<CommunicationCategory> findAllByApartmentId(Apartment apartment);
    List<CommunicationCategory> findAllByApartmentIdAndType(Apartment apartment, CommunicationType communicationType);
}
