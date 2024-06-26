package com.fastcampus.aptner.post.announcement.service.admin;

import com.fastcampus.aptner.apartment.domain.Apartment;
import com.fastcampus.aptner.apartment.service.ApartmentCommonService;
import com.fastcampus.aptner.global.error.RestAPIException;
import com.fastcampus.aptner.jwt.util.JWTMemberInfoDTO;
import com.fastcampus.aptner.post.announcement.domain.Announcement;
import com.fastcampus.aptner.post.announcement.domain.AnnouncementCategory;
import com.fastcampus.aptner.post.announcement.dto.AnnouncementDTO;
import com.fastcampus.aptner.post.announcement.repository.AnnouncementCategoryRepository;
import com.fastcampus.aptner.post.common.error.PostErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Objects;

import static com.fastcampus.aptner.post.common.error.PostErrorCode.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnnouncementCategoryAdminServiceImpl implements AnnouncementCategoryAdminService {

    private final AnnouncementCategoryRepository announcementCategoryRepository;

    private final ApartmentCommonService apartmentService;

    @Override
    public ResponseEntity<HttpStatus> createAnnouncementCategory(JWTMemberInfoDTO userToken, Long apartmentId, AnnouncementDTO.AnnouncementCategoryReqDTO dto) {
        isCorrectApartment(userToken, apartmentId);
        Apartment apartment = apartmentService.getApartmentById(apartmentId);
        announcementCategoryRepository.save(AnnouncementCategory.from(apartment, dto));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseEntity<HttpStatus> updateAnnouncementCategory(JWTMemberInfoDTO userToken, Long announcementCategoryId, AnnouncementDTO.AnnouncementCategoryReqDTO dto) {
        AnnouncementCategory announcementCategory = getAnnouncementCategory(announcementCategoryId);
        checkApartmentByAnnouncementCategory(userToken, announcementCategory);
        announcementCategory.updateCategory(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<HttpStatus> deleteAnnouncementCategory(JWTMemberInfoDTO userToken, Long announcementCategoryId) {
        AnnouncementCategory announcementCategory = getAnnouncementCategory(announcementCategoryId);
        checkApartmentByAnnouncementCategory(userToken, announcementCategory);
        if (!announcementCategory.getAnnouncementList().isEmpty()) {
            throw new RestAPIException(CANT_DELETE);
        }
        announcementCategoryRepository.delete(announcementCategory);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private static void isCorrectApartment(JWTMemberInfoDTO userToken, Long apartmentId) {
        if (userToken.getApartmentId() != apartmentId) {
            throw new RestAPIException(PostErrorCode.NOT_ALLOWED_APARTMENT);
        }
    }

    private void checkApartmentByAnnouncementCategory(JWTMemberInfoDTO userToken, AnnouncementCategory announcementCategory) {
        if (!Objects.equals(userToken.getApartmentId(), announcementCategory.getApartmentId().getApartmentId())) {
            throw new RestAPIException(PostErrorCode.NOT_ALLOWED_APARTMENT);
        }
    }

    private AnnouncementCategory getAnnouncementCategory(Long announcementCategoryId){
        return  announcementCategoryRepository.findById(announcementCategoryId).orElseThrow(()->new RestAPIException(NO_SUCH_CATEGORY));
    }
}
