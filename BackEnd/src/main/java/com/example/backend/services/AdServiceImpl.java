package com.example.backend.services;


import com.example.backend.models.Ad;
import com.example.backend.models.Member;
import com.example.backend.models.MemberInfo;
import com.example.backend.repositories.AdRepository;
import com.example.backend.repositories.MemberRepository;
import com.example.backend.repositories.SkillsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdServiceImpl implements AdService {

    private final AdRepository adRep;
    private final MemberRepository memberRep;
    private final SkillsRepository skillsRep;
    private final FriendService friendService;
    private final MemberService memberService;

    @Override
    public Ad saveAd(Ad ad) {
        log.info("Saving new ad {} to the db", ad.getId());
        return adRep.save(ad);
    }

    @Override
    public void addAdv(String email, Ad ad) {
        Ad saved_ad = this.saveAd(ad);
        Member member = memberRep.findByEmail(email);
        member.getAds().add(saved_ad);
    }

    @Override
    public List<Ad> getFriendsAds(String email) {
        List<MemberInfo> array = friendService.getFriends(email);
        List<Ad> ads = new ArrayList<>();

        // add friends' ads
        for (MemberInfo memberInfo : array)
            ads.addAll(adRep.allAdsofEmail(memberInfo.getId()));
        return ads;
    }

    @Override
    public List<Ad> getNonFriendsAds(String email) {
        List<MemberInfo> friends = friendService.getFriends(email);
        List<Ad> ads_tmp = new ArrayList<>();
        List<Ad> ads_final = new ArrayList<>();
        List<Object[]> list = adRep.getAllAds();
        Member member = memberService.findMemberByEmail(email);

        // get skills of member and put them per word in a list
        String skills_text = skillsRep.getSkills(email);
        String[] member_skills = skills_text.replaceAll("[,]", " ").split("\\s+");

        // if member has no friends
        if (friends.size() == 0) {
            for (Object[] object : list) {
                Long mem_id = Long.parseLong(object[0].toString());

                // adds all adverts that weren't made by the member
                if (!mem_id.equals(member.getId())) {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        ads_tmp.add(new Ad(Long.parseLong(object[1].toString()),
                                object[6].toString(), formatter.parse(object[2].toString()),
                                object[3].toString(), object[5].toString(),
                                object[4].toString()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // else if member has friends
        else {
            for (Object[] object : list) {
                for (MemberInfo friend : friends) {
                    Long mem_id = Long.parseLong(object[0].toString());

                    System.out.println("Member that published ad id: " + mem_id);
                    System.out.println("Logged in member id: " + member.getId());

                    // if the advert isn't published by the same member or a friend, add it to ads_tmp
                    if (!mem_id.equals(member.getId())) {
                        if (!mem_id.equals(friend.getId())) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            try {
                                ads_tmp.add(new Ad(Long.parseLong(object[1].toString()),
                                        object[6].toString(), formatter.parse(object[2].toString()),
                                        object[3].toString(), object[5].toString(),
                                        object[4].toString()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }
        }

        // choose which non friends ads to show based on skills
        ads_tmp.forEach(ad -> {
            List<String> ad_prerequisite_skills = Arrays.asList(ad.getPrerequisite_skills().replaceAll("[,]", " ").split("\\s+"));

            // if member has at least one skill from the prerequisites
            // add the advert to the ads_final list and go to the next one
            for (String member_skill : member_skills) {
                if (ad_prerequisite_skills.contains(member_skill)) {
                    ads_final.add(ad);
                    break;
                }
            }
        });

        return ads_final;
    }



    @Override
    public List<Ad> getOwnAds(String email) {
        Member member = memberRep.findByEmail(email);
        return new ArrayList<>(adRep.allAdsofEmail(member.getId()));
    }


    @Override
    public void applyForAd(String email, Long ad_id) {
        Member member = memberRep.findByEmail(email);
        adRep.apply(member.getId(), ad_id);
    }

    @Override
    public void unApplyForAd(String email, Long ad_id) {
        Member member = memberRep.findByEmail(email);
        adRep.unApply(member.getId(), ad_id);
    }

    @Override
    public BigInteger isAppliedAd(String email, Long id) {
        Member member = memberRep.findByEmail(email);
        return adRep.isApplied(member.getId(), id);
    }

    @Override
    public List<BigInteger> appliedAds(List<Long> ad_ids, String email) {
        List<BigInteger> list = new ArrayList<>();
        ad_ids.forEach(id -> list.add(isAppliedAd(email, id)));
        return list;
    }

    @Override
    public List<MemberInfo> getAppliedMembers(Long id) {
        List<Object[]> list1 = adRep.allAppliedMembersOfAd(id);

        List<MemberInfo> memberInfo = new ArrayList<>();
        list1.forEach(object ->
                memberInfo.add(new MemberInfo(Long.parseLong(object[0].toString()), object[2].toString(), object[5].toString(), object[1].toString(), null)));

        return memberInfo;
    }

    @Override
    public List<List<MemberInfo>> getAllAppliedMembers(List<Long> ad_ids) {
        List<List<MemberInfo>> list = new ArrayList<>();
        ad_ids.forEach(id -> list.add(getAppliedMembers(id)));
        return list;
    }
}
