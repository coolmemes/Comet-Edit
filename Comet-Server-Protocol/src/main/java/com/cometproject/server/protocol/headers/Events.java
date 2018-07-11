package com.cometproject.server.protocol.headers;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class Events {
    public static final short SongInventoryMessageEvent = 3987; // PRODUCTION-201711211204-412329988
    public static final short SongIdMessageEvent = 3508; // PRODUCTION-201711211204-412329988
    public static final short SongDataMessageEvent = 3007; // PRODUCTION-201711211204-412329988
    public static final short PlaylistMessageEvent = 3301; // PRODUCTION-201711211204-412329988
    public static final short PlaylistAddMessageEvent = 883; // PRODUCTION-201711211204-412329988
    public static final short PlaylistRemoveMessageEvent = 2282; // PRODUCTION-201711211204-412329988

    public static final short StaffPickRoomMessageEvent = 3774; // PRODUCTION-201711211204-412329988
    public static final short SubmitPollAnswerMessageEvent = 2422; // PRODUCTION-201711211204-412329988
    public static final short GetPollMessageEvent = 3747; // PRODUCTION-201711211204-412329988
    public static final short RemoveMyRightsMessageEvent = 1363; // PRODUCTION-201711211204-412329988
    public static final short GiveHandItemMessageEvent = 2078; // PRODUCTION-201711211204-412329988
    public static final short InitTradeMessageEvent = 451; // PRODUCTION-201711211204-412329988
    public static final short ChatMessageEvent = 2278; // PRODUCTION-201711211204-412329988
    public static final short GoToHotelViewMessageEvent = 2788; // PRODUCTION-201711211204-412329988
    public static final short GetRoomFilterListMessageEvent = 1104; // PRODUCTION-201711211204-412329988
    public static final short GetForumStatsMessageEvent = 1027; // PRODUCTION-201711211204-412329988
    public static final short GetPromoArticlesMessageEvent = 3332; // PRODUCTION-201711211204-412329988
    public static final short GetCatalogPageMessageEvent = 3213; // PRODUCTION-201711211204-412329988
    public static final short ModifyWhoCanRideHorseMessageEvent = 950; // PRODUCTION-201711211204-412329988
    public static final short RemoveBuddyMessageEvent = 3605; // PRODUCTION-201711211204-412329988
    public static final short RefreshCampaignMessageEvent = 2092; // PRODUCTION-201711211204-412329988
    public static final short AcceptBuddyMessageEvent = 2606; // PRODUCTION-201711211204-412329988
    public static final short FollowFriendMessageEvent = 2275; // PRODUCTION-201711211204-412329988
    public static final short TradingRemoveItemMessageEvent = 2712; // PRODUCTION-201711211204-412329988
    public static final short SaveBotActionMessageEvent = 1230; // PRODUCTION-201711211204-412329988
    public static final short GetGroupCreationWindowMessageEvent = 1180; // PRODUCTION-201711211204-412329988
    public static final short LetUserInMessageEvent = 3673; // PRODUCTION-201711211204-412329988
    public static final short InfoRetrieveMessageEvent = 3627; // PRODUCTION-201711211204-412329988
    public static final short CancelQuestMessageEvent = 3842; // PRODUCTION-201711211204-412329988
    public static final short GetBadgesMessageEvent = 2378; // PRODUCTION-201711211204-412329988
    public static final short GenerateSecretKeyMessageEvent = 2408; // PRODUCTION-201711211204-412329988
    public static final short GetSellablePetBreedsMessageEvent = 841; // PRODUCTION-201711211204-412329988
    public static final short ForceOpenCalendarBoxMessageEvent = 3115;//error 404
    public static final short DeleteRoomMessageEvent = 1698; // PRODUCTION-201711211204-412329988
    public static final short SetSoundSettingsMessageEvent = 3894; // PRODUCTION-201711211204-412329988
    public static final short GetGroupFurniSettingsMessageEvent = 1804; // PRODUCTION-201711211204-412329988
    public static final short ModerateRoomMessageEvent = 1993; // PRODUCTION-201711211204-412329988
    public static final short DeclineGroupMembershipMessageEvent = 1289; // PRODUCTION-201711211204-412329988
    public static final short JoinGroupMessageEvent = 2184; // PRODUCTION-201711211204-412329988
    public static final short ConfirmLoveLockMessageEvent = 1869; // PRODUCTION-201711211204-412329988
    public static final short UseHabboWheelMessageEvent = 1785; // PRODUCTION-201711211204-412329988
    public static final short OpenFlatConnectionMessageEvent = 2503; // PRODUCTION-201711211204-412329988
    public static final short TradingOfferItemsMessageEvent = 970; // PRODUCTION-201711211204-412329988
    public static final short SaveRoomSettingsMessageEvent = 1134; // PRODUCTION-201711211204-412329988
    public static final short DropHandItemMessageEvent = 1250; // PRODUCTION-201711211204-412329988
    public static final short ToggleMoodlightMessageEvent = 1793; // PRODUCTION-201711211204-412329988
    public static final short SetMannequinNameMessageEvent = 2415; // PRODUCTION-201711211204-412329988
    public static final short UseOneWayGateMessageEvent = 3330; // PRODUCTION-201711211204-412329988
    public static final short EventTrackerMessageEvent = 789; // PRODUCTION-201711211204-412329988
    public static final short TradingConfirmMessageEvent = 2609; // PRODUCTION-201711211204-412329988
    public static final short PickUpPetMessageEvent = 665; // PRODUCTION-201711211204-412329988
    public static final short UpdateGroupColoursMessageEvent = 3444; // PRODUCTION-201711211204-412329988
    public static final short GetPetInventoryMessageEvent = 2599; // PRODUCTION-201711211204-412329988
    public static final short InitializeFloorPlanSessionMessageEvent = 2536; // PRODUCTION-201711211204-412329988
    public static final short RequestFurniInventoryMessageEvent = 3674; // PRODUCTION-201711211204-412329988
    public static final short GoToFlatMessageEvent = 2851; // PRODUCTION-201711211204-412329988
    public static final short CheckPetNameMessageEvent = 151; // PRODUCTION-201711211204-412329988
    public static final short RemoveRightsMessageEvent = 1006; // PRODUCTION-201711211204-412329988
    public static final short KickUserMessageEvent = 3149; // PRODUCTION-201711211204-412329988
    public static final short GetRoomSettingsMessageEvent = 1469; // PRODUCTION-201711211204-412329988
    public static final short GetThreadsListDataMessageEvent = 2541; // PRODUCTION-201711211204-412329988
    public static final short GetForumUserProfileMessageEvent = 1218; // PRODUCTION-201711211204-412329988
    public static final short SaveWiredEffectConfigMessageEvent = 440; // PRODUCTION-201711211204-412329988
    public static final short GetAchievementsMessageEvent = 2618; // PRODUCTION-201711211204-412329988
    public static final short GetRoomEntryDataMessageEvent = 3235; // PRODUCTION-201711211204-412329988
    public static final short GetModeratorUserChatlogMessageEvent = 2026; // PRODUCTION-201711211204-412329988
    public static final short CanCreateRoomMessageEvent = 3150; // PRODUCTION-201711211204-412329988
    public static final short SetTonerMessageEvent = 932; // PRODUCTION-201711211204-412329988
    public static final short GetModeratorTicketChatlogsMessageEvent = 3086; // PRODUCTION-201711211204-412329988
    public static final short SetGroupFavouriteMessageEvent = 1178; // PRODUCTION-201711211204-412329988
    public static final short ModerationCautionMessageEvent = 3138; // PRODUCTION-201711211204-412329988
    public static final short SaveWiredTriggerConfigMessageEvent = 3248; // PRODUCTION-201711211204-412329988
    public static final short PlaceBotMessageEvent = 2322; // PRODUCTION-201711211204-412329988
    public static final short GetRelationshipsMessageEvent = 2114; // PRODUCTION-201711211204-412329988
    public static final short ModerationBanMessageEvent = 2915; // PRODUCTION-201711211204-412329988
    public static final short SetMessengerInviteStatusMessageEvent = 3335; // PRODUCTION-201711211204-412329988
    public static final short UseFurnitureMessageEvent = 3272; // PRODUCTION-201711211204-412329988
    public static final short GetUserFlatCatsMessageEvent = 1922; // PRODUCTION-201711211204-412329988
    public static final short AssignRightsMessageEvent = 194; // PRODUCTION-201711211204-412329988
    public static final short GetRoomBannedUsersMessageEvent = 2818; // PRODUCTION-201711211204-412329988
    public static final short ReleaseTicketMessageEvent = 3165; // PRODUCTION-201711211204-412329988
    public static final short OpenPlayerProfileMessageEvent = 878; // PRODUCTION-201711211204-412329988
    public static final short CreditFurniRedeemMessageEvent = 474; // PRODUCTION-201711211204-412329988
    public static final short ActionMessageEvent = 2448; // PRODUCTION-201711211204-412329988
    public static final short PickupObjectMessageEvent = 1539; // PRODUCTION-201711211204-412329988
    public static final short TradingCancelMessageEvent = 1294; // PRODUCTION-201711211204-412329988  1753
    public static final short TradingConfirmCancelMessageEvent = 3054; // up
    public static final short MoveObjectMessageEvent = 3240; // PRODUCTION-201711211204-412329988
    public static final short LookToMessageEvent = 1403; // PRODUCTION-201711211204-412329988
    public static final short InitCryptoMessageEvent = 384; // PRODUCTION-201711211204-412329988
    public static final short GetFurnitureAliasesMessageEvent = 3386; // PRODUCTION-201711211204-412329988
    public static final short TakeAdminRightsMessageEvent = 1083; // PRODUCTION-201711211204-412329988
    public static final short ModifyRoomFilterListMessageEvent = 2646; // PRODUCTION-201711211204-412329988
    public static final short MoodlightUpdateMessageEvent = 3621; // PRODUCTION-201711211204-412329988
    public static final short GetPetTrainingPanelMessageEvent = 1585; // PRODUCTION-201711211204-412329988
    public static final short GetGroupMembersMessageEvent = 667; // PRODUCTION-201711211204-412329988
    public static final short GetModeratorRoomChatlogMessageEvent = 35; // PRODUCTION-201711211204-412329988
    public static final short UseWallItemMessageEvent = 2879; // PRODUCTION-201711211204-412329988
    public static final short GiveAdminRightsMessageEvent = 3876; // PRODUCTION-201711211204-412329988
    public static final short PostGroupContentMessageEvent = 3545; // PRODUCTION-201711211204-412329988
    public static final short GetModeratorUserInfoMessageEvent = 2021; // PRODUCTION-201711211204-412329988
    public static final short SaveWiredConditionConfigMessageEvent = 1071; // PRODUCTION-201711211204-412329988
    public static final short GetModeratorRoomInfoMessageEvent = 2266; // PRODUCTION-201711211204-412329988
    public static final short TradingOfferItemMessageEvent = 3978; // PRODUCTION-201711211204-412329988

    public static final short RedeemVoucherMessageEvent = 1119; // PRODUCTION-201711211204-412329988
    public static final short ThrowDiceMessageEvent = 3308; // PRODUCTION-201711211204-412329988
    public static final short ModerationKickMessageEvent = 593; // PRODUCTION-201711211204-412329988
    public static final short GetModeratorUserRoomVisitsMessageEvent = 413; // PRODUCTION-201711211204-412329988
    public static final short SetRelationshipMessageEvent = 3500; // PRODUCTION-201711211204-412329988
    public static final short RequestBuddyMessageEvent = 456; // PRODUCTION-201711211204-412329988
    public static final short SubmitNewTicketMessageEvent = 1467; // PRODUCTION-201711211204-412329988
    public static final short PurchaseFromCatalogAsGiftMessageEvent = 1420; // PRODUCTION-201711211204-412329988
    public static final short ToggleYouTubeVideoMessageEvent = 2891; // PRODUCTION-201711211204-412329988
    public static final short SetMannequinFigureMessageEvent = 504; // PRODUCTION-201711211204-412329988
    public static final short GetEventCategoriesMessageEvent = 2679; // PRODUCTION-201711211204-412329988
    public static final short DeleteGroupThreadMessageEvent = 2154; // PRODUCTION-201711211204-412329988
    public static final short ApplySignMessageEvent = 2002; // PRODUCTION-201711211204-412329988
    public static final short StartQuestMessageEvent = 2066; // PRODUCTION-201711211204-412329988
    public static final short PurchaseGroupMessageEvent = 882; // PRODUCTION-201711211204-412329988
    public static final short MessengerInitMessageEvent = 337; // PRODUCTION-201711211204-412329988
    public static final short CancelTypingMessageEvent = 1112; // PRODUCTION-201711211204-412329988
    public static final short GetMoodlightConfigMessageEvent = 3221; // PRODUCTION-201711211204-412329988
    public static final short OpenHelpToolMessageEvent = 742; // PRODUCTION-201711211204-412329988
    public static final short GetGroupInfoMessageEvent = 1015; // PRODUCTION-201711211204-412329988
    public static final short CreateFlatMessageEvent = 2164; // PRODUCTION-201711211204-412329988
    public static final short GetWardrobeMessageEvent = 220; // PRODUCTION-201711211204-412329988
    public static final short LatencyTestMessageEvent = 501; // PRODUCTION-201711211204-412329988
    public static final short ChangeMottoMessageEvent = 60; // PRODUCTION-201711211204-412329988
    public static final short GetSelectedBadgesMessageEvent = 527; // PRODUCTION-201711211204-412329988
    public static final short AddStickyNoteMessageEvent = 3572; // PRODUCTION-201711211204-412329988
    public static final short RideHorseMessageEvent = 3863; // PRODUCTION-201711211204-412329988
    public static final short GetCatalogIndexMessageEvent = 3029; // PRODUCTION-201711211204-412329988
    public static final short InitializeNewNavigatorMessageEvent = 3808; // PRODUCTION-201711211204-412329988
    public static final short SetChatPreferenceMessageEvent = 463; // PRODUCTION-201711211204-412329988
    public static final short GetForumsListDataMessageEvent = 1637; // PRODUCTION-201711211204-412329988
    public static final short ToggleMuteToolMessageEvent = 1573; // PRODUCTION-201711211204-412329988
    public static final short UpdateGroupIdentityMessageEvent = 3283; // PRODUCTION-201711211204-412329988
    public static final short UpdateStickyNoteMessageEvent = 2843; // PRODUCTION-201711211204-412329988
    public static final short UnbanUserFromRoomMessageEvent = 1694; // PRODUCTION-201711211204-412329988
    public static final short UnIgnoreUserMessageEvent = 209; // PRODUCTION-201711211204-412329988
    public static final short OpenGiftMessageEvent = 1821; // PRODUCTION-201711211204-412329988
    public static final short ApplyDecorationMessageEvent = 3767; // PRODUCTION-201711211204-412329988
    public static final short ScrGetUserInfoMessageEvent = 661; // PRODUCTION-201711211204-412329988
    public static final short RemoveGroupMemberMessageEvent = 828; // PRODUCTION-201711211204-412329988
    public static final short DiceOffMessageEvent = 418; // PRODUCTION-201711211204-412329988
    public static final short YouTubeGetNextVideo = 1232; // PRODUCTION-201711211204-412329988
    public static final short GetQuestListMessageEvent = 3712; // PRODUCTION-201711211204-412329988
    public static final short RespectUserMessageEvent = 3136; // PRODUCTION-201711211204-412329988
    public static final short DeclineBuddyMessageEvent = 884; // PRODUCTION-201711211204-412329988
    public static final short StartTypingMessageEvent = 563; // PRODUCTION-201711211204-412329988
    public static final short GetGroupFurniConfigMessageEvent = 1810; // PRODUCTION-201711211204-412329988
    public static final short SendRoomInviteMessageEvent = 2992; // PRODUCTION-201711211204-412329988
    public static final short RemoveAllRightsMessageEvent = 163; // PRODUCTION-201711211204-412329988
    public static final short GetYouTubeTelevisionMessageEvent = 2957; // PRODUCTION-201711211204-412329988
    public static final short GetPromotableRoomsMessageEvent = 401; // PRODUCTION-201711211204-412329988
    public static final short GetBotInventoryMessageEvent = 3244; // PRODUCTION-201711211204-412329988
    public static final short PurchaseFromCatalogMessageEvent = 3371; // PRODUCTION-201711211204-412329988
    public static final short OpenBotActionMessageEvent = 141; // PRODUCTION-201711211204-412329988
    public static final short GetClientVersionMessageEvent = 4000; // PRODUCTION-201711211204-412329988
    public static final short ModerationMuteMessageEvent = 750; // PRODUCTION-201711211204-412329988
    public static final short UpdateGroupBadgeMessageEvent = 1707; // PRODUCTION-201711211204-412329988
    public static final short PlaceObjectMessageEvent = 3993; // PRODUCTION-201711211204-412329988
    public static final short RemoveGroupFavouriteMessageEvent = 46; // PRODUCTION-201711211204-412329988
    public static final short UpdateNavigatorSettingsMessageEvent = 910; // PRODUCTION-201711211204-412329988
    public static final short UniqueIDMessageEvent = 1865; // PRODUCTION-201711211204-412329988
    public static final short NewNavigatorSearchMessageEvent = 2221; // PRODUCTION-201711211204-412329988
    public static final short GetPetInformationMessageEvent = 2963; // PRODUCTION-201711211204-412329988
    public static final short GetGuestRoomMessageEvent = 3688; // PRODUCTION-201711211204-412329988
    public static final short UpdateThreadMessageEvent = 547; // PRODUCTION-201711211204-412329988
    public static final short AcceptGroupMembershipMessageEvent = 3582; // PRODUCTION-201711211204-412329988
    public static final short SitMessageEvent = 1531; // PRODUCTION-201711211204-412329988
    public static final short RemoveSaddleFromHorseMessageEvent = 3699; // PRODUCTION-201711211204-412329988
    public static final short GiveRoomScoreMessageEvent = 1307; // PRODUCTION-201711211204-412329988
    public static final short DeleteStickyNoteMessageEvent = 2563; // PRODUCTION-201711211204-412329988
    public static final short MuteUserMessageEvent = 928; // PRODUCTION-201711211204-412329988
    public static final short ApplyHorseEffectMessageEvent = 2409; // PRODUCTION-201711211204-412329988
    public static final short SSOTicketMessageEvent = 1266; // PRODUCTION-201711211204-412329988
    public static final short HabboSearchMessageEvent = 329; // PRODUCTION-201711211204-412329988
    public static final short PickTicketMessageEvent = 1690; // PRODUCTION-201711211204-412329988
    public static final short UpdateFigureDataMessageEvent = 2203; // PRODUCTION-201711211204-412329988
    public static final short GetGiftWrappingConfigurationMessageEvent = 1913; // PRODUCTION-201711211204-412329988
    public static final short GetThreadDataMessageEvent = 3134; // PRODUCTION-201711211204-412329988
    public static final short ManageGroupMessageEvent = 201; // PRODUCTION-201711211204-412329988
    public static final short PlacePetMessageEvent = 1871; // PRODUCTION-201711211204-412329988
    public static final short EditRoomPromotionMessageEvent = 3821; // PRODUCTION-201711211204-412329988
    public static final short GetCatalogOfferMessageEvent = 1786; // PRODUCTION-201711211204-412329988
    public static final short SaveFloorPlanModelMessageEvent = 2872; // PRODUCTION-201711211204-412329988
    public static final short WhisperMessageEvent = 1039; // PRODUCTION-201711211204-412329988
    public static final short MoveWallItemMessageEvent = 2101; // PRODUCTION-201711211204-412329988
    public static final short ShoutMessageEvent = 3681; // PRODUCTION-201711211204-412329988
    public static final short PingMessageEvent = 1337; // PRODUCTION-201711211204-412329988
    public static final short DeleteGroupMessageEvent = 1638; // PRODUCTION-201711211204-412329988
    public static final short UpdateGroupSettingsMessageEvent = 2323; // PRODUCTION-201711211204-412329988
    public static final short PurchaseRoomPromotionMessageEvent = 3516; // PRODUCTION-201711211204-412329988
    public static final short PickUpBotMessageEvent = 3841; // PRODUCTION-201711211204-412329988
    public static final short DanceMessageEvent = 2035; // PRODUCTION-201711211204-412329988
    public static final short ModeratorActionMessageEvent = 3544; // PRODUCTION-201711211204-412329988
    public static final short SaveWardrobeOutfitMessageEvent = 2545; // PRODUCTION-201711211204-412329988
    public static final short SetActivatedBadgesMessageEvent = 623; // PRODUCTION-201711211204-412329988
    public static final short MoveAvatarMessageEvent = 3559; // PRODUCTION-201711211204-412329988
    public static final short SaveBrandingItemMessageEvent = 2211; // PRODUCTION-201711211204-412329988
    public static final short TradingCancelConfirmMessageEvent = 1889;//error 404
    public static final short TradingAcceptMessageEvent = 2991; // PRODUCTION-201711211204-412329988
    public static final short RespectPetMessageEvent = 3845; // PRODUCTION-201711211204-412329988
    public static final short UpdateMagicTileMessageEvent = 3327; // PRODUCTION-201711211204-412329988
    public static final short GetStickyNoteMessageEvent = 1594; // PRODUCTION-201711211204-412329988
    public static final short IgnoreUserMessageEvent = 2927; // PRODUCTION-201711211204-412329988
    public static final short BanUserMessageEvent = 3118; // PRODUCTION-201711211204-412329988
    public static final short UpdateForumSettingsMessageEvent = 2311; // PRODUCTION-201711211204-412329988
    public static final short ModerationMsgMessageEvent = 1542; // PRODUCTION-201711211204-412329988
    public static final short GetRoomRightsMessageEvent = 596; // PRODUCTION-201711211204-412329988
    public static final short SendMsgMessageEvent = 522; // PRODUCTION-201711211204-412329988
    public static final short CloseTicketMesageEvent = 1401; // PRODUCTION-201711211204-412329988

    public static final short UpdateSnapshotsMessageEvent = -1;//error 404

    public static final short GetGameListMessageEvent = 512; // up
    public static final short GetGameAchievementsMessageEvent = 1324; // up
    public static final short GetGameStatusMessageEvent = 3646; // up
    public static final short JoinGameQueueMessageEvent = 2463; // up

    public static final short GetUserTagsMessageEvent = 3893;

    public static final short OpenCalendarBoxMessageEvent = 3778; // PRODUCTION-201711211204-412329988

    public static final short SendAmbassadorAlertMessageEvent = 2601; // PRODUCTION-201711211204-412329988
    public static final short RequestBonusRareMessageEvent = 2944; // PRODUCTION-201711211204-412329988
    public static final short CommunityGoalMessageEvent = 2151; // PRODUCTION-201711211204-412329988
    public static final short VoteCommunityGoalMessageEvent = 858; // PRODUCTION-201711211204-412329988
    public static final short ConcurrentUsersGoalMessageEvent = 271; // PRODUCTION-201711211204-412329988
    public static final short ConcurrentUsersGoalRewardMessageEvent = 1343; // PRODUCTION-201711211204-412329988
    public static final short LTDCountdownMessageEvent = 662; // PRODUCTION-201711211204-412329988
    public static final short GetNextLTDAvaibleMessageEvent = 2969; // PRODUCTION-201711211204-412329988

    public static final short UpdateFocusPreferenceMessageEvent = 208; // PRODUCTION-201711211204-412329988
    public static final short BuyTargetedOfferMessageEvent = 2841; // PRODUCTION-201711211204-412329988
    public static final short NewUserExperienceGiftOfferParserEvent = 2161; // PRODUCTION-201711211204-412329988
    public static final short SpectateRoomMessageEvent = 3703; // PRODUCTION-201711211204-412329988

    public static final short OpenGuideToolMessageEvent = 742; // PRODUCTION-201711211204-412329988
    public static final short RequestGuideAssistanceMessageEvent = 1319; // PRODUCTION-201711211204-412329988
    public static final short GuideUserTypingMessageEvent = 1807; // PRODUCTION-201711211204-412329988
    public static final short GuideReportHelperMessageEvent = 2977; // PRODUCTION-201711211204-412329988
    public static final short GuideRecommendHelperMessageEvent = 1001; // PRODUCTION-201711211204-412329988
    public static final short GuideUserMessageMessageEvent = 3352; // PRODUCTION-201711211204-412329988
    public static final short GuideCancelHelpRequestMessageEvent = 945; // PRODUCTION-201711211204-412329988
    public static final short GuideHandleHelpRequestMessageEvent = 3096; // PRODUCTION-201711211204-412329988
    public static final short GuideVisitUserMessageEvent = 498; // PRODUCTION-201711211204-412329988
    public static final short GuideInviteUserMessageEvent = 3575; // PRODUCTION-201711211204-412329988
    public static final short GuideCloseHelpRequestMessageEvent = 36; // PRODUCTION-201711211204-412329988
    public static final short GuardianNoUpdatesWantedMessageEvent = 94; // PRODUCTION-201711211204-412329988
    public static final short GuardianVoteMessageEvent = 3656; // PRODUCTION-201711211204-412329988
    public static final short GuardianAcceptRequestMessageEvent = 1882; // PRODUCTION-201711211204-412329988
    public static final short GuardianHandleReportMessageEvent = 2352; // PRODUCTION-201711211204-412329988

    public static final short RequestReportUserBullyingMessageEvent = 2710; // PRODUCTION-201711211204-412329988
    public static final short ReportBullyMessageEvent = 1560; // PRODUCTION-201711211204-412329988
    public static final short SaveFootballGateMessageEvent = 2186; // PRODUCTION-201711211204-412329988
    public static final short RedeemClothesMessageEvent = 2804; // PRODUCTION-201711211204-412329988
    public static final short DeleteNavigatorSavedSearchMessageEvent = 2856; // PRODUCTION-201711211204-412329988
    public static final short NuxAcceptMessageEvent = 2724; // PRODUCTION-201711211204-412329988
    public static final short AddFavoriteRoomMessageEvent = 1195; // PRODUCTION-201711211204-412329988
    public static final short RemoveFavoriteRoomMessageEvent = 3502; // PRODUCTION-201711211204-412329988

    public static final short ReportFriendMessageEvent = 926; // PRODUCTION-201711211204-412329988

    public static final short CameraPhotoPreviewMessageEvent = 2907;
    public static final short CameraPhotoPurchaseMessageEvent = 3401;
    public static final short CameraPhotoPublishMessageEvent = 3340;
    public static final short CameraPriceMessageEvent = 2182;
    public static final short CameraThumbnailMessageEvent = 3809;

    public static final short GetFurniMaticBoxesPageMessageEvent = 1471;
    public static final short GetFurniMaticFinalBoxMessageEvent = 1975;
    public static final short GetFurniMaticRewardsPageMessageEvent = 2966;
    public static final short DeleteGroupReplyMessageEvent = 41;

    // Room Competitions
    public static final short RoomCompetitionVoteMessageEvent = 796;
    public static final short RoomCompetitionPublishMessageEvent = 1385;
    public static final short GetRoomCompetitionMessageEvent = 1139;
    public static final short RoomCompetitionSendPhotoMessageEvent = 336;

    // Marketplace
    public static final short GetOffersMessageEvent = 776; // up
    public static final short GetMarketplaceSettingsMessageEvent = 660; // up
    public static final short CanMakeOfferMessageEvent = 741; // up
    public static final short MakeOfferMessageEvent = 42; // up
    public static final short GetOwnOffersMessageEvent = 49; // up
    public static final short CancelOfferMessageEvent = 3571; // up
    public static final short BuyOfferMessageEvent = 1776; // up
    public static final short RedeemCoinsMessageEvent = 2738; // up
    public static final short GetMarketplaceItemStatsMessageEvent = 1102; // up

    public static final short ChangeNameMessageEvent = 2350;
    public static final short CheckValidNameMessageEvent = 3487;

    // Crafting
    public static final short GetCraftingRecipesAvailableMessageEvent = 3714; // 1680
    public static final short ExecuteCraftingRecipeMessageEvent = 537;
    public static final short CraftSecretMessageEvent = 2086;
    public static final short ViewCraftingRecipeMessageEvent = 1680;
    public static final short GetCraftingItemMessageEvent = 677;

    // SMS
    public static final short GetSMSWindowMessageEvent = 3294;
    public static final short AcceptSMSVerificationMessageEvent = 768;
    public static final short VerifySMSMessageEvent = 2587;
    public static final short VerifyEmailMessageEvent = 1468;

    // Subscription
    public static final short GetSubscriptionCenterMessageEvent = 1669;
    public static final short GetSubscriptionPageMessageEvent = 1093;
    public static final short GetPresentsPageMessageEvent = 507;
    public static final short GetClubPresentMessageEvent = 1482;

    // Talent Track
    public static final short GetTalentTrackMessageEvent = 2215;
    public static final short SafetyQuizAnsweredMessageEvent = 108;

    private static Map<Short, String> eventPacketNames = new HashMap<>();

    static {
        try {
            for (Field field : Events.class.getDeclaredFields()) {
                if (!Modifier.isPrivate(field.getModifiers()))
                    eventPacketNames.put(field.getShort(field.getName()), field.getName());
            }
        } catch (Exception ignored) {

        }
    }

    public static String valueOfId(short packetId) {
        if (eventPacketNames.containsKey(packetId)) {
            return eventPacketNames.get(packetId);
        }

        return "UnknownMessageEvent";
    }
}
