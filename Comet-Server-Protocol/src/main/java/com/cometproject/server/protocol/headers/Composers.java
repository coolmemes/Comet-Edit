package com.cometproject.server.protocol.headers;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

//outgoing
public class Composers {
    public static final short SongInventoryMessageComposer = 1955; // PRODUCTION-201711211204-412329988
    public static final short SongIdMessageComposer = 2233; // PRODUCTION-201711211204-412329988
    public static final short PlaylistMessageComposer = 2381; // PRODUCTION-201711211204-412329988
    public static final short PlayMusicMessageComposer = 2158; // PRODUCTION-201711211204-412329988
    public static final short QuickPollMessageComposer = 3174; // PRODUCTION-201711211204-412329988
    public static final short QuickPollResultMessageComposer = 2906; // PRODUCTION-201711211204-412329988
    public static final short QuickPollResultsMessageComposer = 604; // PRODUCTION-201711211204-412329988
    public static final short CatalogItemDiscountMessageComposer = 2352; // PRODUCTION-201711211204-412329988
    public static final short RoomErrorNotifMessageComposer = 2998; // PRODUCTION-201711211204-412329988
    public static final short PurchaseOKMessageComposer = 1861; // PRODUCTION-201711211204-412329988
    public static final short ModeratorInitMessageComposer = 3743; // PRODUCTION-201711211204-412329988
    public static final short CatalogOfferMessageComposer = 3433; // PRODUCTION-201711211204-412329988
    public static final short ThreadsListDataMessageComposer = 1246; // PRODUCTION-201711211204-412329988
    public static final short UserChangeMessageComposer = 1126; // PRODUCTION-201711211204-412329988
    public static final short FloorHeightMapMessageComposer = 3091; // PRODUCTION-201711211204-412329988
    public static final short RoomInfoUpdatedMessageComposer = 1626; // PRODUCTION-201711211204-412329988
    public static final short ScrSendUserInfoMessageComposer = 3730; // PRODUCTION-201711211204-412329988
    public static final short RoomSettingsDataMessageComposer = 2897; // PRODUCTION-201711211204-412329988
    public static final short OpenGiftMessageComposer = 2058; // PRODUCTION-201711211204-412329988
    public static final short SlideObjectBundleMessageComposer = 2112; // PRODUCTION-201711211204-412329988
    public static final short StickyNoteMessageComposer = 2293; // PRODUCTION-201711211204-412329988
    public static final short UserRemoveMessageComposer = 1506; // PRODUCTION-201711211204-412329988
    public static final short GetGuestRoomResultMessageComposer = 2172; // PRODUCTION-201711211204-412329988
    public static final short DoorbellMessageComposer = 2541; // PRODUCTION-201711211204-412329988
    public static final short AvailabilityStatusMessageComposer = 1939; // PRODUCTION-201711211204-412329988
    public static final short GiftWrappingConfigurationMessageComposer = 938; // PRODUCTION-201711211204-412329988
    public static final short QuestStartedMessageComposer = 943; // PRODUCTION-201711211204-412329988
    public static final short NavigatorLiftedRoomsMessageComposer = 3958; // PRODUCTION-201711211204-412329988
    public static final short NavigatorPreferencesMessageComposer = 1107; // PRODUCTION-201711211204-412329988
    public static final short TradingFinishMessageComposer = 406; // PRODUCTION-201711211204-412329988
    public static final short GetRelationshipsMessageComposer = 3926; // PRODUCTION-201711211204-412329988
    public static final short WhisperMessageComposer = 1338; // PRODUCTION-201711211204-412329988
    public static final short BadgeEditorPartsMessageComposer = 627; // PRODUCTION-201711211204-412329988
    public static final short TraxSongInfoMessageComposer = 295; // PRODUCTION-201711211204-412329988
    public static final short GroupFurniConfigMessageComposer = 2971; // PRODUCTION-201711211204-412329988
    public static final short PostUpdatedMessageComposer = 3929; // PRODUCTION-201711211204-412329988
    public static final short UserUpdateMessageComposer = 1434; // PRODUCTION-201711211204-412329988
    public static final short ModeratorUserRoomVisitsMessageComposer = 2114; // PRODUCTION-201711211204-412329988
    public static final short MutedMessageComposer = 1582; // PRODUCTION-201711211204-412329988
    public static final short ChatMessageComposer = 2899; // PRODUCTION-201711211204-412329988
    public static final short ShoutMessageComposer = 1870; // PRODUCTION-201711211204-412329988
    public static final short ThreadCreatedMessageComposer = 3698; // PRODUCTION-201711211204-412329988
    public static final short GroupCreationWindowMessageComposer = 3155; // PRODUCTION-201711211204-412329988
    public static final short OpenBotActionMessageComposer = 3267; // PRODUCTION-201711211204-412329988
    public static final short ThreadDataMessageComposer = 1522; // PRODUCTION-201711211204-412329988
    public static final short YouAreOwnerMessageComposer = 2683; // PRODUCTION-201711211204-412329988
    public static final short RoomForwardMessageComposer = 1823; // PRODUCTION-201711211204-412329988
    public static final short FavouritesMessageComposer = 908; // PRODUCTION-201711211204-412329988
    public static final short RoomSettingsSavedMessageComposer = 2075; // PRODUCTION-201711211204-412329988
    public static final short RoomReadyMessageComposer = 3657; // PRODUCTION-201711211204-412329988
    public static final short QuestAbortedMessageComposer = 2794; // PRODUCTION-201711211204-412329988
    public static final short CampaignMessageComposer = 3763; // PRODUCTION-201711211204-412329988
    public static final short CatalogPageMessageComposer = 523; // PRODUCTION-201711211204-412329988
    public static final short RoomEventMessageComposer = 1000; // PRODUCTION-201711211204-412329988
    public static final short ObjectRemoveMessageComposer = 2511; // PRODUCTION-201711211204-412329988
    public static final short AchievementScoreMessageComposer = 617; // PRODUCTION-201711211204-412329988
    public static final short ModeratorRoomChatlogMessageComposer = 2393; // PRODUCTION-201711211204-412329988
    public static final short WiredConditionConfigMessageComposer = 929; // PRODUCTION-201711211204-412329988
    public static final short SellablePetBreedsMessageComposer = 2089; // PRODUCTION-201711211204-412329988
    public static final short BuddyListMessageComposer = 1053; // PRODUCTION-201711211204-412329988
    public static final short HabboSearchResultMessageComposer = 1118; // PRODUCTION-201711211204-412329988
    public static final short ItemUpdateMessageComposer = 3634; // PRODUCTION-201711211204-412329988
    public static final short PetHorseFigureInformationMessageComposer = 1250; // PRODUCTION-201711211204-412329988
    public static final short PetInventoryMessageComposer = 3232; // PRODUCTION-201711211204-412329988
    public static final short MoodlightConfigMessageComposer = 508; // PRODUCTION-201711211204-412329988
    public static final short PongMessageComposer =-1;//error 404
    public static final short GroupMembersMessageComposer = 85; // PRODUCTION-201711211204-412329988
    public static final short GetYouTubePlaylistMessageComposer = 1995; // PRODUCTION-201711211204-412329988
    public static final short RespectNotificationMessageComposer = 1004; // PRODUCTION-201711211204-412329988
    public static final short SleepMessageComposer = 3978; // PRODUCTION-201711211204-412329988
    public static final short GetRoomBannedUsersMessageComposer = 2509; // PRODUCTION-201711211204-412329988
    public static final short ModeratorUserInfoMessageComposer = 3047; // PRODUCTION-201711211204-412329988
    public static final short WiredTriggerConfigMessageComposer = 1670; // PRODUCTION-201711211204-412329988
    public static final short RoomRatingMessageComposer = 3339; // PRODUCTION-201711211204-412329988
    public static final short ModeratorSupportTicketResponseMessageComposer = 1922; // PRODUCTION-201711211204-412329988
    public static final short UserObjectMessageComposer = 3064; // PRODUCTION-201711211204-412329988
    public static final short WardrobeMessageComposer = 3123; // PRODUCTION-201711211204-412329988
    public static final short FurniListNotificationMessageComposer = 116; // PRODUCTION-201711211204-412329988
    public static final short ModeratorUserChatlogMessageComposer = 1816; // PRODUCTION-201711211204-412329988
    public static final short ThreadUpdatedMessageComposer = 263; // PRODUCTION-201711211204-412329988
    public static final short LoveLockDialogueMessageComposer = 3172; // PRODUCTION-201711211204-412329988
    public static final short ThreadReplyMessageComposer = 1450; // PRODUCTION-201711211204-412329988
    public static final short QuestListMessageComposer = 1208; // PRODUCTION-201711211204-412329988
    public static final short FriendListUpdateMessageComposer = 988; // PRODUCTION-201711211204-412329988
    public static final short NavigatorFlatCatsMessageComposer = 2228; // PRODUCTION-201711211204-412329988
    public static final short UserFlatCatsMessageComposer = 264; // PRODUCTION-201711211204-412329988
    public static final short HideWiredConfigMessageComposer = 3508; // PRODUCTION-201711211204-412329988
    public static final short ActivityPointsMessageComposer = 3289; // PRODUCTION-201711211204-412329988
    public static final short UnbanUserFromRoomMessageComposer = 1385; // PRODUCTION-201711211204-412329988
    public static final short AvatarEffectMessageComposer = 3639; // PRODUCTION-201711211204-412329988
    public static final short PetTrainingPanelMessageComposer = 412; // PRODUCTION-201711211204-412329988
    public static final short LoveLockDialogueCloseMessageComposer = 451; // PRODUCTION-201711211204-412329988
    public static final short FurniListRemoveMessageComposer = 2096; // PRODUCTION-201711211204-412329988
    public static final short BuildersClubMembershipMessageComposer = 1517; // PRODUCTION-201711211204-412329988
    public static final short SecretKeyMessageComposer = 2334; // PRODUCTION-201711211204-412329988
    public static final short CloseConnectionMessageComposer = 2364; // PRODUCTION-201711211204-412329988
    public static final short HabboActivityPointNotificationMessageComposer = 2802; // PRODUCTION-201711211204-412329988
    public static final short NavigatorMetaDataParserMessageComposer = 1855; // PRODUCTION-201711211204-412329988
    public static final short NavigatorCollapsedCategoriesMessageComposer = 1695; // PRODUCTION-201711211204-412329988
    public static final short FlatAccessDeniedMessageComposer = 3016; // PRODUCTION-201711211204-412329988
    public static final short LatencyResponseMessageComposer = 2213; // PRODUCTION-201711211204-412329988
    public static final short BuddyRequestsMessageComposer = 1397; // PRODUCTION-201711211204-412329988
    public static final short HabboUserBadgesMessageComposer = 574; // PRODUCTION-201711211204-412329988
    public static final short HeightMapMessageComposer = 3602; // PRODUCTION-201711211204-412329988
    public static final short ObjectUpdateMessageComposer = 3621; // PRODUCTION-201711211204-412329988
    public static final short YouAreControllerMessageComposer = 2627; // PRODUCTION-201711211204-412329988
    public static final short CatalogIndexMessageComposer = 3173; // PRODUCTION-201711211204-412329988
    public static final short FlatControllerRemovedMessageComposer = 2152; // PRODUCTION-201711211204-412329988
    public static final short NewBuddyRequestMessageComposer = 154; // PRODUCTION-201711211204-412329988
    public static final short CanCreateRoomMessageComposer = 434; // PRODUCTION-201711211204-412329988
    public static final short ModeratorRoomInfoMessageComposer = 3251; // PRODUCTION-201711211204-412329988
    public static final short FloodControlMessageComposer = 1424; // PRODUCTION-201711211204-412329988
    public static final short RoomRightsListMessageComposer = 947; // PRODUCTION-201711211204-412329988
    public static final short CfhTopicsInitMessageComposer = 1386; // PRODUCTION-201711211204-412329988
    public static final short IgnoreStatusMessageComposer = 1744; // PRODUCTION-201711211204-412329988
    public static final short PetInformationMessageComposer = 3915; // PRODUCTION-201711211204-412329988
    public static final short NavigatorSearchResultSetMessageComposer = 1598; // PRODUCTION-201711211204-412329988
    public static final short GroupInfoMessageComposer = 1110; // PRODUCTION-201711211204-412329988
    public static final short UsersMessageComposer = 3550; // PRODUCTION-201711211204-412329988
    public static final short ItemRemoveMessageComposer = 3667; // PRODUCTION-201711211204-412329988
    public static final short GetYouTubeVideoMessageComposer = 875; // PRODUCTION-201711211204-412329988
    public static final short GenericErrorMessageComposer = 2106; // PRODUCTION-201711211204-412329988
    public static final short UserRightsMessageComposer = 1124; // PRODUCTION-201711211204-412329988
    public static final short ItemAddMessageComposer = 3779; // PRODUCTION-201711211204-412329988
    public static final short CheckPetNameMessageComposer = 480; // PRODUCTION-201711211204-412329988
    public static final short RespectPetNotificationMessageComposer = 3364; // PRODUCTION-201711211204-412329988
    public static final short EnforceCategoryUpdateMessageComposer = 1876; // PRODUCTION-201711211204-412329988
    public static final short ActionMessageComposer = 3771; // PRODUCTION-201711211204-412329988
    public static final short ModeratorSupportTicketMessageComposer = 2462; // PRODUCTION-201711211204-412329988
    public static final short AchievementsMessageComposer = 403; // PRODUCTION-201711211204-412329988
    public static final short FloorPlanFloorMapMessageComposer = 996; // PRODUCTION-201711211204-412329988
    public static final short AchievementUnlockedMessageComposer = 1435; // PRODUCTION-201711211204-412329988
    public static final short GiftWrappingErrorMessageComposer = 2985; // PRODUCTION-201711211204-412329988
    public static final short OpenConnectionMessageComposer = 761; // PRODUCTION-201711211204-412329988
    public static final short TradingClosedMessageComposer = 1542; // PRODUCTION-201711211204-412329988
    public static final short PromoArticlesMessageComposer = 2512; // PRODUCTION-201711211204-412329988
    public static final short AddExperiencePointsMessageComposer = 1041; // PRODUCTION-201711211204-412329988
    public static final short OpenHelpToolMessageComposer = 1588; // PRODUCTION-201711211204-412329988
    public static final short CreditBalanceMessageComposer = 3204; // PRODUCTION-201711211204-412329988
    public static final short QuestCompletedMessageComposer = 628; // PRODUCTION-201711211204-412329988
    public static final short CarryObjectMessageComposer = 3408; // PRODUCTION-201711211204-412329988
    public static final short InitCryptoMessageComposer = 3182; // PRODUCTION-201711211204-412329988
    public static final short PromotableRoomsMessageComposer = 2706; // PRODUCTION-201711211204-412329988
    public static final short TradingCompleteMessageComposer = 655; // PRODUCTION-201711211204-412329988
    public static final short FloorPlanSendDoorMessageComposer = 621; // PRODUCTION-201711211204-412329988
    public static final short FurniListMessageComposer = 1356; // PRODUCTION-201711211204-412329988
    public static final short RoomEntryInfoMessageComposer = 2620; // PRODUCTION-201711211204-412329988
    public static final short CatalogUpdatedMessageComposer = 3579; // PRODUCTION-201711211204-412329988
    public static final short SetUniqueIdMessageComposer = 1954; // PRODUCTION-201711211204-412329988
    public static final short FurniListUpdateMessageComposer = 165; // PRODUCTION-201711211204-412329988
    public static final short NewGroupInfoMessageComposer = 989; // PRODUCTION-201711211204-412329988
    public static final short RoomNotificationMessageComposer = 1178; // PRODUCTION-201711211204-412329988
    public static final short MOTDNotificationMessageComposer = 3551; // PRODUCTION-201711211204-412329988
    public static final short HabboGroupBadgesMessageComposer = 2165; // PRODUCTION-201711211204-412329988
    public static final short PopularRoomTagsResultMessageComposer = 1054; // PRODUCTION-201711211204-412329988
    public static final short NewConsoleMessageMessageComposer = 260; // PRODUCTION-201711211204-412329988
    public static final short RoomPropertyMessageComposer = 1154; // PRODUCTION-201711211204-412329988
    public static final short TradingUpdateMessageComposer = 3760; // PRODUCTION-201711211204-412329988
    public static final short GroupFurniSettingsMessageComposer = 3167; // PRODUCTION-201711211204-412329988
    public static final short ProfileInformationMessageComposer = 1455; // PRODUCTION-201711211204-412329988
    public static final short BadgeDefinitionsMessageComposer = 1173; // PRODUCTION-201711211204-412329988
    public static final short SoundSettingsMessageComposer = 2317; // PRODUCTION-201711211204-412329988
    public static final short UserTypingMessageComposer = 2108; // PRODUCTION-201711211204-412329988
    public static final short RoomVisualizationSettingsMessageComposer = 45; // PRODUCTION-201711211204-412329988
    public static final short UserPerksMessageComposer = 66; // PRODUCTION-201711211204-412329988
    public static final short ForumsListDataMessageComposer = 3582; // PRODUCTION-201711211204-412329988
    public static final short DanceMessageComposer = 1911; // PRODUCTION-201711211204-412329988
    public static final short FlatCreatedMessageComposer = 2479; // PRODUCTION-201711211204-412329988
    public static final short BotInventoryMessageComposer = 185; // PRODUCTION-201711211204-412329988
    public static final short ObjectsMessageComposer = 1559; // PRODUCTION-201711211204-412329988
    public static final short ItemsMessageComposer = 2400; // PRODUCTION-201711211204-412329988
    public static final short AvatarEffectsMessageComposer = 3868; // PRODUCTION-201711211204-412329988
    public static final short ManageGroupMessageComposer = 3438; // PRODUCTION-201711211204-412329988
    public static final short FurnitureAliasesMessageComposer = 1216; // PRODUCTION-201711211204-412329988
    public static final short MaintenanceStatusMessageComposer = 447; // PRODUCTION-201711211204-412329988
    public static final short BadgesMessageComposer = 1919; // PRODUCTION-201711211204-412329988
    public static final short WiredEffectConfigMessageComposer = 720; // PRODUCTION-201711211204-412329988
    public static final short RoomMuteSettingsMessageComposer = 1493; // PRODUCTION-201711211204-412329988
    public static final short RoomInviteMessageComposer = 3342; // PRODUCTION-201711211204-412329988
    public static final short LoveLockDialogueSetLockedMessageComposer = 451; // PRODUCTION-201711211204-412329988
    public static final short BroadcastMessageAlertMessageComposer = 1367; // PRODUCTION-201711211204-412329988
    public static final short ForumDataMessageComposer = 3962; // PRODUCTION-201711211204-412329988
    public static final short AchievementProgressedMessageComposer = 3027; // PRODUCTION-201711211204-412329988
    public static final short RefreshFavouriteGroupMessageComposer = 777; // PRODUCTION-201711211204-412329988
    public static final short TradingErrorMessageComposer = 825; // PRODUCTION-201711211204-412329988
    public static final short ObjectAddMessageComposer = 2873; // PRODUCTION-201711211204-412329988
    public static final short TradingAcceptMessageComposer = 446; // PRODUCTION-201711211204-412329988
    public static final short AuthenticationOKMessageComposer = 1523; // PRODUCTION-201711211204-412329988
    public static final short TradingStartMessageComposer = 1033; // PRODUCTION-201711211204-412329988
    public static final short NavigatorSettingsMessageComposer = 456; // PRODUCTION-201711211204-412329988
    public static final short FlatControllerAddedMessageComposer = 2667; // PRODUCTION-201711211204-412329988
    public static final short ModeratorTicketChatlogMessageComposer = 2181; // PRODUCTION-201711211204-412329988
    public static final short MessengerInitMessageComposer = 2496; // PRODUCTION-201711211204-412329988
    public static final short InitializePollMessageComposer = 313; // PRODUCTION-201711211204-412329988
    public static final short PollMessageComposer = 3751; // PRODUCTION-201711211204-412329988
    public static final short AvatarAspectUpdateMessageComposer = 1412; // PRODUCTION-201711211204-412329988
    public static final short YouAreSpectatorMessageComposer = 301;//error 404
    public static final short UpdateStackMapMessageComposer = 3442; // PRODUCTION-201711211204-412329988

    public static final short HideDoorbellUserMessageComposer = 650; // PRODUCTION-201711211204-412329988
    public static final short FriendNotificationMessageComposer = 1800; // PRODUCTION-201711211204-412329988
    public static final short HideAvatarOptionsMessageComposer = 3534; // PRODUCTION-201711211204-412329988
    public static final short RoomQueueStatusMessageComposer = 169;//error 404

    public static final short TargetedOfferMessageComposer = 2003; // PRODUCTION-201711211204-412329988
    public static final short NuxGiftSelectionViewMessageComposer = 787; // PRODUCTION-201711211204-412329988
    public static final short HallOfFameMessageComposer = 2577; // PRODUCTION-201711211204-412329988
    public static final short BonusRareMessageComposer = 337; // PRODUCTION-201711211204-412329988
    public static final short CommunityGoalMessageComposer = 317; // PRODUCTION-201711211204-412329988
    public static final short DynamicPollLandingComposer = 3463; // PRODUCTION-201711211204-412329988
    public static final short ConcurrentUsersGoalProgressMessageComposer = 348; // PRODUCTION-201711211204-412329988
    public static final short LTDCountdownMessageComposer = 2348; // PRODUCTION-201711211204-412329988
    public static final short NextLTDAvaibleMessageComposer = 1235; // PRODUCTION-201711211204-412329988
    public static final short NuxAlertMessageComposer = 2197; // PRODUCTION-201711211204-412329988
    public static final short WiredRewardMessageComposer = 289; // PRODUCTION-201711211204-412329988

    public static final short GuideSessionAttachedMessageComposer = 919; // PRODUCTION-201711211204-412329988
    public static final short GuideSessionDetachedMessageComposer = 753; // PRODUCTION-201711211204-412329988
    public static final short GuideSessionStartedMessageComposer = 3928; // PRODUCTION-201711211204-412329988
    public static final short GuideSessionEndedMessageComposer = 3223; // PRODUCTION-201711211204-412329988
    public static final short GuideSessionErrorMessageComposer = 2133; // PRODUCTION-201711211204-412329988
    public static final short GuideSessionMessageMessageComposer = 2554; // PRODUCTION-201711211204-412329988
    public static final short GuideSessionRequesterRoomMessageComposer = 1067; // PRODUCTION-201711211204-412329988
    public static final short GuideSessionInvitedToGuideRoomMessageComposer = 2554; // PRODUCTION-201711211204-412329988
    public static final short GuideSessionPartnerIsTypingMessageComposer = 141; // PRODUCTION-201711211204-412329988

    public static final short GuideToolsMessageComposer = 1357; // PRODUCTION-201711211204-412329988
    public static final short GuardianNewReportReceivedMessageComposer = 3759; // PRODUCTION-201711211204-412329988
    public static final short GuardianVotingRequestedMessageComposer = 1774; // PRODUCTION-201711211204-412329988
    public static final short GuardianVotingVotesMessageComposer = 3882; // PRODUCTION-201711211204-412329988
    public static final short GuardianVotingResultMessageComposer = 2967; // PRODUCTION-201711211204-412329988
    public static final short GuardianVotingTimeEndedMessageComposer = 753; // PRODUCTION-201711211204-412329988

    public static final short BullyReportClosedMessageComposer = 2438; // PRODUCTION-201711211204-412329988
    public static final short BullyReportRequestMessageComposer = 2316; // PRODUCTION-201711211204-412329988
    public static final short BullyReportedMessageMessageComposer = 236; // PRODUCTION-201711211204-412329988

    public static final short HelperRequestDisabledMessageComposer = 631; // PRODUCTION-201711211204-412329988

    public static final short CampaignCalendarMessageComposer = 680; // PRODUCTION-201711211204-412329988
    public static final short CampaignCalendarGiftMessageComposer = 2059; // PRODUCTION-201711211204-412329988

    public static final short CameraRenderImageMessageComposer = 1949;
    public static final short CameraPurchasedMessageComposer = 340;
    public static final short CameraPublishedMessageComposer = 2411;
    public static final short CameraPriceMessageComposer = 3069;
    public static final short CameraThumbnailUpdatedMessageComposer = 690;

    public static final short LimitedEditionSoldOutMessageComposer = -1;
    public static final short AvatarChangeNameMessageComposer = -1;

    public static final short RecyclerSuccessMessageComposer = 2597;
    public static final short RecyclerErrorMessageComposer = 1395;
    public static final short RecyclerRewardsMessageComposer = 822;

    public static final short HotelAlertLinkMessageComposer = 2994;
    public static final short UpdateAvatarNameMessageComposer = -1;
    public static final short RoomActionMessageComposer = 804;
    public static final short UserNameChangeMessageComposer = 3170;
    public static final short RoomEnterErrorMessageComposer = 3264;
    public static final short FlatAccessibleMessageComposer = -1;

    public static final short UpdateFreezeLivesMessageComposer = 3395;
    public static final short GetRoomFilterListMessageComposer = 1888;

    public static final short GameListMessageComposer = 1831; // up
    public static final short GameAchievementsMessageComposer = 1362; // up
    public static final short GameStatusMessageComposer = 2934; // up
    public static final short GameAccountStatusMessageComposer = 3833; // up
    public static final short GameLoadMessageComposer = 1605; // up

    public static final short UserTagsMessageComposer = 2713;

    public static final short RoomCompetitionVisitorInfoMessageComposer = 3197;
    public static final short RoomCompetitionOwnerInfoMessageComposer = 3376;

    public static final short NewYearResolutionCompletedMessageComposer = 3869;

    // Marketplace
    public static final short BuyOfferMessageComposer = 1920;
    public static final short CancelOfferMessageComposer = 1808;
    public static final short CanMakeOfferMessageComposer = 1232;
    public static final short ItemStatsMessageComposer = 2852;
    public static final short MakeOfferMessageComposer = 1436;
    public static final short MarketplaceSettingsMessageComposer = 2539;
    public static final short OffersMessageComposer = 1719;
    public static final short OwnOffersMessageComposer = 1610;

    // Subscriptions
    public static final short SubscriptionGiftAlertMessageComposer = 91;
    public static final short SubscriptionRemainingAlertMessageComposer = 2455;
    public static final short SubscriptionCenterInfoMessageComposer = 1310;
    public static final short CatalogGiftsPageMessageComposer = 3399;

    // Notifications
    public static final short HabboEpicPopupViewMessageComposer = 944;
    public static final short CreateLinkEventMessageComposer = 2197;
    public static final short UnknownNotificationMessageComposer = 649;
    public static final short UsernameUpdateMessageComposer = 72;
    public static final short NameChangeVerificationMessageComposer = 3338;
    public static final short HCRequiredNotificationMessageComposer = 1624;

    // Crafting
    public static final short CraftableProductsMessageComposer = 3471;
    public static final short CraftableProductsToGetResultMessageComposer = 1771;
    public static final short CraftingFinalResultMessageComposer = 1925;
    public static final short CraftingRecipeResultMessageComposer = 1945;

    // SMS - Email
    public static final short SMSVerificationOfferMessageComposer = 279;
    public static final short SMSVerificationWindowMessageComposer = 1676;
    public static final short SMSVerificationCompleteMessageComposer = 2180;
    public static final short EmailVerificationWindowMessageComposer = 2449;

    // Rewards
    public static final short RewardListMessageComposer = 813;
    public static final short TalentTrackMessageComposer = 118;
    public static final short TalentTrackProgressMessageComposer = 3258;
    public static final short WatchAndEarnEnabledMessageComposer = 1616;

    // Minimail
    public static final short MinimailCountMessageComposer = 2619;
    public static final short NewMinimailMessageComposer = 1110;

    public static final short HabboMall = 1379;

    // Resolution
    public static final short NewYearResolutionMessageComposer = 2186;

    private static Map<Short, String> composerPacketNames = new HashMap<>();

    static {
        try {
            for (Field field : Composers.class.getDeclaredFields()) {
                if (!Modifier.isPrivate(field.getModifiers()))
                    composerPacketNames.put(field.getShort(field.getName()), field.getName());
            }
        } catch (Exception ignored) {

        }
    }

    public static String valueOfId(short packetId) {
        if (composerPacketNames.containsKey(packetId)) {
            return composerPacketNames.get(packetId);
        }

        return "UnknownMessageComposer";
    }
}