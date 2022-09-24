package com.example.backend.repositories;

import com.example.backend.models.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface AdRepository  extends JpaRepository<Ad, Long> {

    @Query(value = "SELECT a.* FROM member_ads ma, ad a WHERE ma.member_id=:id && a.id = ma.ads_id",
            nativeQuery = true)
    List<Ad> allAdsofEmail(Long id);

    @Modifying
    @Query(value = "INSERT INTO application " +
            "VALUES(:member_id, :ad_id)",  nativeQuery = true)
    void apply(Long member_id, Long ad_id);

    @Modifying
    @Query(value = "DELETE FROM application " +
            "WHERE application.member_id = :member_id AND application.ad_id = :ad_id",  nativeQuery = true)
    void unApply(Long member_id, Long ad_id);

    @Query(value = "SELECT CASE WHEN COUNT(*)>0 " +
            "THEN TRUE " +
            "ELSE FALSE " +
            "END " +
            "FROM application WHERE member_id = :member_id AND ad_id = :ad_id", nativeQuery = true)
    BigInteger isApplied(Long member_id, Long ad_id);

    @Query(value = "SELECT m.* FROM member m, application ap " +
            "WHERE ap.ad_id =:id AND m.id = ap.member_id", nativeQuery = true)
    List<Object[]> allAppliedMembersOfAd(@Param("id") Long id);


    // gets the prerequisite skills of all ads, together with the id of the member that made the advert
    @Query(value = "SELECT m.member_id, ad.* FROM ad, member_ads m " +
            "WHERE m.ads_id = ad.id", nativeQuery = true)
    List<Object[]> getAllAds();
}
