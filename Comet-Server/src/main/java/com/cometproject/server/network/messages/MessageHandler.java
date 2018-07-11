package com.cometproject.server.network.messages;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.incoming.catalog.*;
import com.cometproject.server.network.messages.incoming.catalog.ads.CatalogPromotionGetRoomsMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.ads.PromoteRoomMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.ads.PromotionUpdateMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.club.GetClubPresentMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.club.GetPresentsPageMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.club.GetSubscriptionPageMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.data.GetGiftWrappingConfigurationMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.groups.BuyGroupDialogMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.groups.BuyGroupMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.groups.GroupFurnitureCatalogMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.pets.PetRacesMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.pets.ValidatePetNameMessageEvent;
import com.cometproject.server.network.messages.incoming.catalog.recycler.GetFurniMaticBoxesPageEvent;
import com.cometproject.server.network.messages.incoming.catalog.recycler.GetFurniMaticFinalBoxEvent;
import com.cometproject.server.network.messages.incoming.catalog.recycler.GetFurniMaticRewardsPageEvent;
import com.cometproject.server.network.messages.incoming.crafting.*;
import com.cometproject.server.network.messages.incoming.gamecenter.*;
import com.cometproject.server.network.messages.incoming.group.*;
import com.cometproject.server.network.messages.incoming.group.favourite.ClearFavouriteGroupMessageEvent;
import com.cometproject.server.network.messages.incoming.group.favourite.SetFavouriteGroupMessageEvent;
import com.cometproject.server.network.messages.incoming.group.forum.data.ForumDataMessageEvent;
import com.cometproject.server.network.messages.incoming.group.forum.data.GetForumsMessageEvent;
import com.cometproject.server.network.messages.incoming.group.forum.settings.SaveForumSettingsMessageEvent;
import com.cometproject.server.network.messages.incoming.group.forum.threads.*;
import com.cometproject.server.network.messages.incoming.group.settings.*;
import com.cometproject.server.network.messages.incoming.handshake.*;
import com.cometproject.server.network.messages.incoming.help.HelpTicketMessageEvent;
import com.cometproject.server.network.messages.incoming.help.InitHelpToolMessageEvent;
import com.cometproject.server.network.messages.incoming.landing.LandingLoadWidgetMessageEvent;
import com.cometproject.server.network.messages.incoming.landing.RefreshPromoArticlesMessageEvent;
import com.cometproject.server.network.messages.incoming.marketplace.*;
import com.cometproject.server.network.messages.incoming.messenger.*;
import com.cometproject.server.network.messages.incoming.moderation.*;
import com.cometproject.server.network.messages.incoming.moderation.guides.guardian.*;
import com.cometproject.server.network.messages.incoming.moderation.ambassador.SendAmbassadorAlertMessageEvent;
import com.cometproject.server.network.messages.incoming.moderation.guides.OpenGuideToolMessageEvent;
import com.cometproject.server.network.messages.incoming.moderation.tickets.ModToolCloseIssueMessageEvent;
import com.cometproject.server.network.messages.incoming.moderation.tickets.ModToolPickTicketMessageEvent;
import com.cometproject.server.network.messages.incoming.moderation.tickets.ModToolReleaseIssueMessageEvent;
import com.cometproject.server.network.messages.incoming.moderation.tickets.ModToolTicketChatlogMessageEvent;
import com.cometproject.server.network.messages.incoming.music.SongDataMessageEvent;
import com.cometproject.server.network.messages.incoming.music.SongIdMessageEvent;
import com.cometproject.server.network.messages.incoming.music.playlist.PlaylistAddMessageEvent;
import com.cometproject.server.network.messages.incoming.music.playlist.PlaylistMessageEvent;
import com.cometproject.server.network.messages.incoming.music.playlist.PlaylistRemoveMessageEvent;
import com.cometproject.server.network.messages.incoming.navigator.*;
import com.cometproject.server.network.messages.incoming.navigator.updated.InitializeNewNavigatorMessageEvent;
import com.cometproject.server.network.messages.incoming.navigator.updated.NewNavigatorSearchMessageEvent;
import com.cometproject.server.network.messages.incoming.nuxs.NewUserExperienceGiftOfferParserEvent;
import com.cometproject.server.network.messages.incoming.performance.EventLogMessageEvent;
import com.cometproject.server.network.messages.incoming.performance.RequestLatencyTestMessageEvent;
import com.cometproject.server.network.messages.incoming.polls.GetPollMessageEvent;
import com.cometproject.server.network.messages.incoming.polls.SubmitPollAnswerMessageEvent;
import com.cometproject.server.network.messages.incoming.quests.CancelQuestMessageEvent;
import com.cometproject.server.network.messages.incoming.quests.OpenQuestsMessageEvent;
import com.cometproject.server.network.messages.incoming.quests.StartQuestMessageEvent;
import com.cometproject.server.network.messages.incoming.room.access.AnswerDoorbellMessageEvent;
import com.cometproject.server.network.messages.incoming.room.access.LoadRoomByDoorBellMessageEvent;
import com.cometproject.server.network.messages.incoming.room.access.SpectateRoomMessageEvent;
import com.cometproject.server.network.messages.incoming.room.action.*;
import com.cometproject.server.network.messages.incoming.room.bots.BotConfigMessageEvent;
import com.cometproject.server.network.messages.incoming.room.bots.ModifyBotMessageEvent;
import com.cometproject.server.network.messages.incoming.room.bots.PlaceBotMessageEvent;
import com.cometproject.server.network.messages.incoming.room.bots.RemoveBotMessageEvent;
import com.cometproject.server.network.messages.incoming.room.competition.*;
import com.cometproject.server.network.messages.incoming.room.engine.*;
import com.cometproject.server.network.messages.incoming.room.filter.EditWordFilterMessageEvent;
import com.cometproject.server.network.messages.incoming.room.filter.WordFilterListMessageEvent;
import com.cometproject.server.network.messages.incoming.room.floor.GetTilesInUseMessageEvent;
import com.cometproject.server.network.messages.incoming.room.floor.SaveFloorMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.*;
import com.cometproject.server.network.messages.incoming.room.item.gifts.OpenGiftMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.lovelock.ConfirmLoveLockMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.mannequins.SaveMannequinFigureMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.mannequins.SaveMannequinMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.stickies.DeletePostItMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.stickies.OpenPostItMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.stickies.PlacePostitMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.stickies.SavePostItMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.wired.SaveWiredDataMessageEvent;
import com.cometproject.server.network.messages.incoming.room.item.wired.UpdateSnapshotsMessageEvent;
import com.cometproject.server.network.messages.incoming.room.moderation.*;
import com.cometproject.server.network.messages.incoming.room.pets.*;
import com.cometproject.server.network.messages.incoming.room.pets.horse.ApplyHorseEffectMessageEvent;
import com.cometproject.server.network.messages.incoming.room.pets.horse.ModifyWhoCanRideHorseMessageEvent;
import com.cometproject.server.network.messages.incoming.room.pets.horse.RemoveHorseSaddleMessageEvent;
import com.cometproject.server.network.messages.incoming.room.pets.horse.RideHorseMessageEvent;
import com.cometproject.server.network.messages.incoming.room.settings.*;
import com.cometproject.server.network.messages.incoming.room.trading.*;
import com.cometproject.server.network.messages.incoming.user.achievements.AchievementsListMessageEvent;
import com.cometproject.server.network.messages.incoming.user.camera.CameraPhotoPreviewMessageEvent;
import com.cometproject.server.network.messages.incoming.user.camera.CameraPhotoPublishMessageEvent;
import com.cometproject.server.network.messages.incoming.user.camera.CameraPhotoPurchaseMessageEvent;
import com.cometproject.server.network.messages.incoming.user.camera.CameraPriceMessageEvent;
import com.cometproject.server.network.messages.incoming.user.club.ClubStatusMessageEvent;
import com.cometproject.server.network.messages.incoming.user.club.GetSubscriptionCenterMessageEvent;
import com.cometproject.server.network.messages.incoming.user.details.*;
import com.cometproject.server.network.messages.incoming.user.inventory.*;
import com.cometproject.server.network.messages.incoming.user.profile.*;
import com.cometproject.server.network.messages.incoming.user.talents.GetTalentTrackMessageEvent;
import com.cometproject.server.network.messages.incoming.user.talents.SafetyQuizAnsweredMessageEvent;
import com.cometproject.server.network.messages.incoming.user.verification.AcceptSMSVerificationMessageEvent;
import com.cometproject.server.network.messages.incoming.user.verification.GetSMSWindowMessageEvent;
import com.cometproject.server.network.messages.incoming.user.verification.VerifyEmailMessageEvent;
import com.cometproject.server.network.messages.incoming.user.verification.VerifySMSMessageEvent;
import com.cometproject.server.network.messages.incoming.user.wardrobe.ChangeLooksMessageEvent;
import com.cometproject.server.network.messages.incoming.user.wardrobe.SaveWardrobeMessageEvent;
import com.cometproject.server.network.messages.incoming.user.wardrobe.WardrobeMessageEvent;
import com.cometproject.server.network.messages.incoming.user.youtube.LoadPlaylistMessageEvent;
import com.cometproject.server.network.messages.incoming.user.youtube.NextVideoMessageEvent;
import com.cometproject.server.network.messages.incoming.user.youtube.PlayVideoMessageEvent;
import com.cometproject.server.network.messages.incoming.group.DeleteGroupMessageEvent;
import com.cometproject.server.network.messages.types.tasks.MessageEventTask;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.headers.Events;
import com.cometproject.server.protocol.messages.MessageEvent;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.*;

public final class MessageHandler {
    public static Logger log = Logger.getLogger(MessageHandler.class.getName());
    private final Map<Short, Event> messages = new ConcurrentHashMap<>();

    private final AbstractExecutorService eventExecutor;
    private final boolean asyncEventExecution;

    public MessageHandler() {
        this.asyncEventExecution = Boolean.parseBoolean((String) Comet.getServer().getConfig().getOrDefault("comet.network.alternativePacketHandling.enabled", "false"));
//        this.eventExecutor = asyncEventExecution ? Executors.newFixedThreadPool(Integer.parseInt((String) Comet.getServer().getConfig().getOrDefault("comet.network.alternativePacketHandling.threads", "8"))) : null;

        if (this.asyncEventExecution) {
            switch ((String) Comet.getServer().getConfig().getOrDefault("comet.network.alternativePacketHandling.type", "threadpool")) {
                default:
                    log.info("Using thread-pool event executor");
                    this.eventExecutor = new ThreadPoolExecutor(Integer.parseInt((String) Comet.getServer().getConfig().getOrDefault("comet.network.alternativePacketHandling.coreSize", "8")), // core size
                            Integer.parseInt((String) Comet.getServer().getConfig().getOrDefault("comet.network.alternativePacketHandling.maxSize", "32")), // max size
                            10 * 60, // idle timeout
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>());
                    break;

                case "forkjoin":
                    log.info("Using fork-join event executor");
                    this.eventExecutor = new ForkJoinPool(Integer.parseInt((String) Comet.getServer().getConfig().getOrDefault("comet.network.alternativePacketHandling.coreSize", 16)), ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
                    break;
            }
        } else {
            this.eventExecutor = null;
        }

        this.load();
    }

    public void load() {
        this.registerHandshake();
        this.registerModTool();
        this.registerHelpTool();
        this.registerMessenger();
        this.registerNavigator();
        this.registerUser();
        this.registerBots();
        this.registerRoom();
        this.registerRoomTrade();
        this.registerRoomModeration();
        this.registerRoomAccess();
        this.registerItems();
        this.registerCatalog();
        this.registerPets();
        this.registerLanding();
        this.registerGroups();
        this.registerGroupForums();
        this.registerQuests();
        this.registerPromotions();
        this.registerAchievements();
        this.registerPolls();
        this.registerMisc();
        this.registerMusic();
        this.registerCamera();
        this.registerGameCenter();
        this.registerMarketplace();
        this.registerCrafting();

        log.info("Loaded " + this.getMessages().size() + " message events");
    }

    private void registerMisc() {
        this.getMessages().put(Events.LatencyTestMessageEvent, new RequestLatencyTestMessageEvent());
        this.getMessages().put(Events.EventTrackerMessageEvent, new EventLogMessageEvent());
    }

    public void registerHandshake() {
        this.getMessages().put(Events.GetClientVersionMessageEvent, new CheckReleaseMessageEvent());
        this.getMessages().put(Events.InitCryptoMessageEvent, new InitCryptoMessageEvent());
        this.getMessages().put(Events.GenerateSecretKeyMessageEvent, new GenerateSecretKeyMessageEvent());
        this.getMessages().put(Events.SSOTicketMessageEvent, new SSOTicketMessageEvent());
        this.getMessages().put(Events.UniqueIDMessageEvent, new UniqueIdMessageEvent());
    }

    public void registerModTool() {
        this.getMessages().put(Events.GetModeratorUserInfoMessageEvent, new ModToolUserInfoMessageEvent());
        this.getMessages().put(Events.GetModeratorUserChatlogMessageEvent, new ModToolUserChatlogMessageEvent());
        this.getMessages().put(Events.GetModeratorRoomChatlogMessageEvent, new ModToolRoomChatlogMessageEvent());
        this.getMessages().put(Events.ModerationBanMessageEvent, new ModToolBanUserMessageEvent());
        this.getMessages().put(Events.GetModeratorRoomInfoMessageEvent, new ModToolRoomInfoMessageEvent());
        this.getMessages().put(Events.GetModeratorUserRoomVisitsMessageEvent, new ModToolRoomVisitsMessageEvent());
        this.getMessages().put(Events.ModerationMsgMessageEvent, new ModToolUserAlertMessageEvent());
        this.getMessages().put(Events.ModerationCautionMessageEvent, new ModToolUserCautionMessageEvent());
        this.getMessages().put(Events.ModerationKickMessageEvent, new ModToolUserKickMessageEvent());
        this.getMessages().put(Events.ModeratorActionMessageEvent, new ModToolRoomAlertMessageEvent());
        this.getMessages().put(Events.ModerateRoomMessageEvent, new ModToolRoomActionMessageEvent());
        this.getMessages().put(Events.PickTicketMessageEvent, new ModToolPickTicketMessageEvent());
        this.getMessages().put(Events.GetModeratorTicketChatlogsMessageEvent, new ModToolTicketChatlogMessageEvent());
        this.getMessages().put(Events.CloseTicketMesageEvent, new ModToolCloseIssueMessageEvent());
        this.getMessages().put(Events.ReleaseTicketMessageEvent, new ModToolReleaseIssueMessageEvent());
        this.getMessages().put(Events.ModerationMuteMessageEvent, new ModerationMuteUserMessageEvent());
    }

    public void registerHelpTool() {
        this.getMessages().put(Events.OpenHelpToolMessageEvent, new InitHelpToolMessageEvent());
        this.getMessages().put(Events.SubmitNewTicketMessageEvent, new HelpTicketMessageEvent());
        this.getMessages().put(Events.OpenGuideToolMessageEvent, new OpenGuideToolMessageEvent());
        this.getMessages().put(Events.GuardianAcceptRequestMessageEvent, new GuardianAcceptRequestMessageEvent());
        this.getMessages().put(Events.GuardianNoUpdatesWantedMessageEvent, new GuardianNoUpdatesWantedMessageEvent());
        this.getMessages().put(Events.GuardianVoteMessageEvent, new GuardianVoteMessageEvent());
        this.getMessages().put(Events.GuardianHandleReportMessageEvent, new GuardianHandleReportMessageEvent());
    }

    public void registerCamera(){
        this.getMessages().put(Events.CameraPhotoPreviewMessageEvent, new CameraPhotoPreviewMessageEvent(false));
        this.getMessages().put(Events.CameraPhotoPublishMessageEvent, new CameraPhotoPublishMessageEvent());
        this.getMessages().put(Events.CameraPhotoPurchaseMessageEvent, new CameraPhotoPurchaseMessageEvent());
        this.getMessages().put(Events.CameraPriceMessageEvent, new CameraPriceMessageEvent());
        this.getMessages().put(Events.CameraThumbnailMessageEvent, new CameraPhotoPreviewMessageEvent(true));
    }

    public void registerMessenger() {
        this.getMessages().put(Events.MessengerInitMessageEvent, new InitializeFriendListMessageEvent());
        this.getMessages().put(Events.SendMsgMessageEvent, new PrivateChatMessageEvent());
        this.getMessages().put(Events.RequestBuddyMessageEvent, new RequestFriendshipMessageEvent());
        this.getMessages().put(Events.AcceptBuddyMessageEvent, new AcceptFriendshipMessageEvent());
        this.getMessages().put(Events.HabboSearchMessageEvent, new SearchFriendsMessageEvent());
        this.getMessages().put(Events.FollowFriendMessageEvent, new FollowFriendMessageEvent());
        this.getMessages().put(Events.RemoveBuddyMessageEvent, new DeleteFriendsMessageEvent());
        this.getMessages().put(Events.SendRoomInviteMessageEvent, new InviteFriendsMessageEvent());
        this.getMessages().put(Events.DeclineBuddyMessageEvent, new DeclineFriendshipMessageEvent());
        this.getMessages().put(Events.ReportFriendMessageEvent, new ReportFriendMessageEvent());
    }

    public void registerNavigator() {
        this.getMessages().put(Events.GetUserFlatCatsMessageEvent, new LoadCategoriesMessageEvent());
        this.getMessages().put(Events.InitializeNewNavigatorMessageEvent, new InitializeNewNavigatorMessageEvent());
        this.getMessages().put(Events.NewNavigatorSearchMessageEvent, new NewNavigatorSearchMessageEvent());
        this.getMessages().put(Events.CanCreateRoomMessageEvent, new CanCreateRoomMessageEvent());
        this.getMessages().put(Events.CreateFlatMessageEvent, new CreateRoomMessageEvent());
        this.getMessages().put(Events.GetEventCategoriesMessageEvent, new EventCategoriesMessageEvent());
        this.getMessages().put(Events.GetPromotableRoomsMessageEvent, new CatalogPromotionGetRoomsMessageEvent());
        this.getMessages().put(Events.StaffPickRoomMessageEvent, new AddToStaffPickedRoomsMessageEvent());
        this.getMessages().put(Events.SpectateRoomMessageEvent, new SpectateRoomMessageEvent());
    }

    public void registerCrafting() {
        this.getMessages().put(Events.GetCraftingRecipesAvailableMessageEvent, new GetCraftingRecipesAvailableMessageEvent());
        this.getMessages().put(Events.CraftSecretMessageEvent, new CraftSecretMessageEvent());
        this.getMessages().put(Events.ViewCraftingRecipeMessageEvent, new ViewCraftingRecipeMessageEvent());
        this.getMessages().put(Events.ExecuteCraftingRecipeMessageEvent, new ExecuteCraftingRecipeMessageEvent());
        this.getMessages().put(Events.GetCraftingItemMessageEvent, new GetCraftingItemMessageEvent());
    }

    public void registerUser() {
        this.getMessages().put(Events.OpenPlayerProfileMessageEvent, new GetProfileMessageEvent());
        this.getMessages().put(Events.GetForumUserProfileMessageEvent, new GetProfileByUsernameMessageEvent());
        this.getMessages().put(Events.ScrGetUserInfoMessageEvent, new ClubStatusMessageEvent());
        this.getMessages().put(Events.InfoRetrieveMessageEvent, new InfoRetrieveMessageEvent());
        this.getMessages().put(Events.UpdateFigureDataMessageEvent, new ChangeLooksMessageEvent());
        this.getMessages().put(Events.RequestFurniInventoryMessageEvent, new OpenInventoryMessageEvent());
        this.getMessages().put(Events.GetBadgesMessageEvent, new BadgeInventoryMessageEvent());
        this.getMessages().put(Events.ChangeMottoMessageEvent, new ChangeMottoMessageEvent());
        this.getMessages().put(Events.GetRelationshipsMessageEvent, new GetRelationshipsMessageEvent());
        this.getMessages().put(Events.SetRelationshipMessageEvent, new SetRelationshipMessageEvent());
        this.getMessages().put(Events.SetActivatedBadgesMessageEvent, new WearBadgeMessageEvent());
        this.getMessages().put(Events.GetWardrobeMessageEvent, new WardrobeMessageEvent());
        this.getMessages().put(Events.SaveWardrobeOutfitMessageEvent, new SaveWardrobeMessageEvent());
        this.getMessages().put(Events.UpdateNavigatorSettingsMessageEvent, new ChangeHomeRoomMessageEvent());
        this.getMessages().put(Events.SetSoundSettingsMessageEvent, new UpdateAudioSettingsMessageEvent());
        this.getMessages().put(Events.SetChatPreferenceMessageEvent, new UpdateChatStyleMessageEvent());
        this.getMessages().put(Events.GetUserTagsMessageEvent, new GetUserTagsMessageEvent());
        this.getMessages().put(Events.ChangeNameMessageEvent, new ChangeNameMessageEvent());
        this.getMessages().put(Events.CheckValidNameMessageEvent, new CheckValidNameMessageEvent());
        this.getMessages().put(Events.GetSMSWindowMessageEvent, new GetSMSWindowMessageEvent());
        this.getMessages().put(Events.AcceptSMSVerificationMessageEvent, new AcceptSMSVerificationMessageEvent());
        this.getMessages().put(Events.VerifySMSMessageEvent, new VerifySMSMessageEvent());
        this.getMessages().put(Events.VerifyEmailMessageEvent, new VerifyEmailMessageEvent());
        this.getMessages().put(Events.GetSubscriptionCenterMessageEvent, new GetSubscriptionCenterMessageEvent());
        this.getMessages().put(Events.UpdateFocusPreferenceMessageEvent, new UpdateFocusPreferenceMessageEvent());
        this.getMessages().put(Events.GetTalentTrackMessageEvent, new GetTalentTrackMessageEvent());
        this.getMessages().put(Events.SafetyQuizAnsweredMessageEvent, new SafetyQuizAnsweredMessageEvent());
    }

    public void registerBots() {
        this.getMessages().put(Events.GetBotInventoryMessageEvent, new BotInventoryMessageEvent());
        this.getMessages().put(Events.PlaceBotMessageEvent, new PlaceBotMessageEvent());
        this.getMessages().put(Events.SaveBotActionMessageEvent, new ModifyBotMessageEvent());
        this.getMessages().put(Events.PickUpBotMessageEvent, new RemoveBotMessageEvent());
        this.getMessages().put(Events.OpenBotActionMessageEvent, new BotConfigMessageEvent());
    }

    public void registerPets() {
        this.getMessages().put(Events.GetPetInventoryMessageEvent, new PetInventoryMessageEvent());
        this.getMessages().put(Events.PlacePetMessageEvent, new PlacePetMessageEvent());
        this.getMessages().put(Events.GetPetInformationMessageEvent, new PetInformationMessageEvent());
        this.getMessages().put(Events.PickUpPetMessageEvent, new RemovePetMessageEvent());
        this.getMessages().put(Events.RideHorseMessageEvent, new RideHorseMessageEvent());
        this.getMessages().put(Events.RespectPetMessageEvent, new ScratchPetMessageEvent());
        this.getMessages().put(Events.GetPetTrainingPanelMessageEvent, new GetPetTrainingPanelMessageEvent());
        this.getMessages().put(Events.ApplyHorseEffectMessageEvent, new ApplyHorseEffectMessageEvent());
        this.getMessages().put(Events.RemoveSaddleFromHorseMessageEvent, new RemoveHorseSaddleMessageEvent());
        this.getMessages().put(Events.ModifyWhoCanRideHorseMessageEvent, new ModifyWhoCanRideHorseMessageEvent());
    }

    public void registerRoom() {
        this.getMessages().put(Events.OpenFlatConnectionMessageEvent, new InitializeRoomMessageEvent());
        this.getMessages().put(Events.GetGuestRoomMessageEvent, new FollowRoomInfoMessageEvent());
        this.getMessages().put(Events.GetRoomEntryDataMessageEvent, new AddUserToRoomMessageEvent());
        this.getMessages().put(Events.GoToHotelViewMessageEvent, new ExitRoomMessageEvent());
        this.getMessages().put(Events.ChatMessageEvent, new TalkMessageEvent());
        this.getMessages().put(Events.ShoutMessageEvent, new ShoutMessageEvent());
        this.getMessages().put(Events.WhisperMessageEvent, new WhisperMessageEvent());
        this.getMessages().put(Events.MoveAvatarMessageEvent, new WalkMessageEvent());
        this.getMessages().put(Events.ActionMessageEvent, new ApplyActionMessageEvent());
        this.getMessages().put(Events.ApplySignMessageEvent, new ApplySignMessageEvent());
        this.getMessages().put(Events.DanceMessageEvent, new ApplyDanceMessageEvent());
        this.getMessages().put(Events.GetRoomSettingsMessageEvent, new GetRoomSettingsDataMessageEvent());
        this.getMessages().put(Events.SaveRoomSettingsMessageEvent, new SaveRoomDataMessageEvent());
        this.getMessages().put(Events.RespectUserMessageEvent, new RespectUserMessageEvent());
        this.getMessages().put(Events.StartTypingMessageEvent, new StartTypingMessageEvent());
        this.getMessages().put(Events.CancelTypingMessageEvent, new StopTypingMessageEvent());
        this.getMessages().put(Events.LookToMessageEvent, new LookToMessageEvent());
        this.getMessages().put(Events.GetSelectedBadgesMessageEvent, new UserBadgesMessageEvent());
        this.getMessages().put(Events.ApplyDecorationMessageEvent, new ApplyDecorationMessageEvent());
        this.getMessages().put(Events.DropHandItemMessageEvent, new DropHandItemMessageEvent());
        this.getMessages().put(Events.DeleteRoomMessageEvent, new DeleteRoomMessageEvent());
        this.getMessages().put(Events.ToggleMuteToolMessageEvent, new MuteRoomMessageEvent());
        this.getMessages().put(Events.GiveRoomScoreMessageEvent, new RateRoomMessageEvent());
        this.getMessages().put(Events.GiveHandItemMessageEvent, new GiveHandItemMessageEvent());
        this.getMessages().put(Events.SaveFloorPlanModelMessageEvent, new SaveFloorMessageEvent());
        this.getMessages().put(Events.InitializeFloorPlanSessionMessageEvent, new GetTilesInUseMessageEvent());
        this.getMessages().put(Events.IgnoreUserMessageEvent, new IgnoreUserMessageEvent());
        this.getMessages().put(Events.UnIgnoreUserMessageEvent, new UnignoreUserMessageEvent());
        this.getMessages().put(Events.RemoveMyRightsMessageEvent, new RemoveOwnRightsMessageEvent());
        this.getMessages().put(Events.SitMessageEvent, new SitMessageEvent());
        this.getMessages().put(Events.GetFurnitureAliasesMessageEvent, new GetFurnitureAliasesMessageEvent());
        this.getMessages().put(Events.GetRoomCompetitionMessageEvent, new GetRoomCompetitionMessageEvent());
        this.getMessages().put(Events.RoomCompetitionPublishMessageEvent, new RoomCompetitionPublishMessageEvent());
        this.getMessages().put(Events.RoomCompetitionVoteMessageEvent, new RoomCompetitionVoteMessageEvent());
        this.getMessages().put(Events.RoomCompetitionSendPhotoMessageEvent, new RoomCompetitionSendPhotoMessageEvent());
    }

    public void registerRoomTrade() {
        this.getMessages().put(Events.InitTradeMessageEvent, new BeginTradeMessageEvent());
        this.getMessages().put(Events.TradingCancelMessageEvent, new CancelTradeMessageEvent());
        this.getMessages().put(Events.TradingCancelConfirmMessageEvent, new UnacceptTradeMessageEvent());
        this.getMessages().put(Events.TradingOfferItemMessageEvent, new SendOfferMessageEvent());
        this.getMessages().put(Events.TradingRemoveItemMessageEvent, new TradingRemoveItemMessageEvent());
        this.getMessages().put(Events.TradingAcceptMessageEvent, new AcceptTradeMessageEvent());
        this.getMessages().put(Events.TradingConfirmMessageEvent, new ConfirmTradeMessageEvent());
        this.getMessages().put(Events.TradingOfferItemsMessageEvent, new TradingOfferItemsMessageEvent());
    }

    public void registerRoomModeration() {
        this.getMessages().put(Events.KickUserMessageEvent, new KickUserMessageEvent());
        this.getMessages().put(Events.BanUserMessageEvent, new BanUserMessageEvent());
        this.getMessages().put(Events.AssignRightsMessageEvent, new GiveRightsMessageEvent());
        this.getMessages().put(Events.RemoveRightsMessageEvent, new RemoveRightsMessageEvent());
        this.getMessages().put(Events.RemoveAllRightsMessageEvent, new RemoveAllRightsMessageEvent());
        this.getMessages().put(Events.GetRoomBannedUsersMessageEvent, new GetBannedUsersMessageEvent());
        this.getMessages().put(Events.UnbanUserFromRoomMessageEvent, new RoomUnbanUserMessageEvent());
        this.getMessages().put(Events.MuteUserMessageEvent, new MutePlayerMessageEvent());
        this.getMessages().put(Events.GetRoomRightsMessageEvent, new UsersWithRightsMessageEvent());
        this.getMessages().put(Events.GetRoomFilterListMessageEvent, new WordFilterListMessageEvent());
        this.getMessages().put(Events.ModifyRoomFilterListMessageEvent, new EditWordFilterMessageEvent());
        this.getMessages().put(Events.SendAmbassadorAlertMessageEvent, new SendAmbassadorAlertMessageEvent());
        this.getMessages().put(Events.ReportBullyMessageEvent, new ReportBullyMessageEvent());
    }

    public void registerRoomAccess() {
        this.getMessages().put(Events.LetUserInMessageEvent, new AnswerDoorbellMessageEvent());
        this.getMessages().put(Events.GoToFlatMessageEvent, new LoadRoomByDoorBellMessageEvent());
    }

    public void registerItems() {
        this.getMessages().put(Events.PlaceObjectMessageEvent, new PlaceItemMessageEvent());
        this.getMessages().put(Events.MoveObjectMessageEvent, new ChangeFloorItemPositionMessageEvent());
        this.getMessages().put(Events.MoveWallItemMessageEvent, new ChangeWallItemPositionMessageEvent());
        this.getMessages().put(Events.PickupObjectMessageEvent, new PickUpItemMessageEvent());
        this.getMessages().put(Events.UseFurnitureMessageEvent, new ChangeFloorItemStateMessageEvent());
        this.getMessages().put(Events.UseOneWayGateMessageEvent, new ChangeFloorItemStateMessageEvent());
        this.getMessages().put(Events.DiceOffMessageEvent, new OpenDiceMessageEvent());
        this.getMessages().put(Events.ThrowDiceMessageEvent, new RunDiceMessageEvent());

        this.getMessages().put(Events.SaveWiredEffectConfigMessageEvent, new SaveWiredDataMessageEvent());
        this.getMessages().put(Events.SaveWiredConditionConfigMessageEvent, new SaveWiredDataMessageEvent());
        this.getMessages().put(Events.SaveWiredTriggerConfigMessageEvent, new SaveWiredDataMessageEvent());
        this.getMessages().put(Events.UpdateSnapshotsMessageEvent, new UpdateSnapshotsMessageEvent());

        this.getMessages().put(Events.CreditFurniRedeemMessageEvent, new ExchangeItemMessageEvent());
        this.getMessages().put(Events.UseWallItemMessageEvent, new UseWallItemMessageEvent());
        this.getMessages().put(Events.UseHabboWheelMessageEvent, new UseWallItemMessageEvent());
        this.getMessages().put(Events.SetMannequinNameMessageEvent, new SaveMannequinMessageEvent());
        this.getMessages().put(Events.SetMannequinFigureMessageEvent, new SaveMannequinFigureMessageEvent());
        this.getMessages().put(Events.SetTonerMessageEvent, new SaveTonerMessageEvent());
        this.getMessages().put(Events.SaveBrandingItemMessageEvent, new SaveBrandingMessageEvent());
        this.getMessages().put(Events.OpenGiftMessageEvent, new OpenGiftMessageEvent());
        this.getMessages().put(Events.GetMoodlightConfigMessageEvent, new UseMoodlightMessageEvent());
        this.getMessages().put(Events.ToggleMoodlightMessageEvent, new ToggleMoodlightMessageEvent());
        this.getMessages().put(Events.MoodlightUpdateMessageEvent, new UpdateMoodlightMessageEvent());
        this.getMessages().put(Events.UpdateMagicTileMessageEvent, new SaveStackToolMessageEvent());
        this.getMessages().put(Events.AddStickyNoteMessageEvent, new PlacePostitMessageEvent());
        this.getMessages().put(Events.GetStickyNoteMessageEvent, new OpenPostItMessageEvent());
        this.getMessages().put(Events.UpdateStickyNoteMessageEvent, new SavePostItMessageEvent());
        this.getMessages().put(Events.DeleteStickyNoteMessageEvent, new DeletePostItMessageEvent());
        this.getMessages().put(Events.GetYouTubeTelevisionMessageEvent, new LoadPlaylistMessageEvent());
        this.getMessages().put(Events.ToggleYouTubeVideoMessageEvent, new PlayVideoMessageEvent());
        this.getMessages().put(Events.YouTubeGetNextVideo, new NextVideoMessageEvent());
        this.getMessages().put(Events.ConfirmLoveLockMessageEvent, new ConfirmLoveLockMessageEvent());
        this.getMessages().put(Events.SaveFootballGateMessageEvent, new SaveFootballGateMessageEvent());
    }

    public void registerPromotions() {
        this.getMessages().put(Events.PurchaseRoomPromotionMessageEvent, new PromoteRoomMessageEvent());
        this.getMessages().put(Events.EditRoomPromotionMessageEvent, new PromotionUpdateMessageEvent());
    }

    private void registerMarketplace() {
        getMessages().put(Events.GetOffersMessageEvent, new GetOffersMessageEvent());
        getMessages().put(Events.GetMarketplaceSettingsMessageEvent, new GetMarketplaceSettingsMessageEvent());
        getMessages().put(Events.CanMakeOfferMessageEvent, new CanMakeOfferMessageEvent());
        getMessages().put(Events.MakeOfferMessageEvent, new MakeOfferMessageEvent());
        getMessages().put(Events.GetOwnOffersMessageEvent, new GetOwnOffersMessageEvent());
        getMessages().put(Events.CancelOfferMessageEvent, new CancelOfferMessageEvent());
        getMessages().put(Events.BuyOfferMessageEvent, new BuyOfferMessageEvent());
        getMessages().put(Events.RedeemCoinsMessageEvent, new RedeemCoinsMessageEvent());
        getMessages().put(Events.GetMarketplaceItemStatsMessageEvent, new GetMarketplaceItemStatsMessageEvent());
    }

    public void registerCatalog() {
        this.getMessages().put(Events.GetCatalogIndexMessageEvent, new GetCataIndexMessageEvent());
        this.getMessages().put(Events.GetCatalogPageMessageEvent, new GetCataPageMessageEvent());
        this.getMessages().put(Events.PurchaseFromCatalogMessageEvent, new PurchaseItemMessageEvent());
        this.getMessages().put(Events.GetGiftWrappingConfigurationMessageEvent, new GetGiftWrappingConfigurationMessageEvent());
        this.getMessages().put(Events.GetGroupCreationWindowMessageEvent, new BuyGroupDialogMessageEvent());
        this.getMessages().put(Events.PurchaseGroupMessageEvent, new BuyGroupMessageEvent());
        this.getMessages().put(Events.GetSellablePetBreedsMessageEvent, new PetRacesMessageEvent());
        this.getMessages().put(Events.CheckPetNameMessageEvent, new ValidatePetNameMessageEvent());
        this.getMessages().put(Events.PurchaseFromCatalogAsGiftMessageEvent, new PurchaseGiftMessageEvent());
        this.getMessages().put(Events.GetGroupFurniConfigMessageEvent, new GroupFurnitureCatalogMessageEvent());
        this.getMessages().put(Events.GetCatalogOfferMessageEvent, new GetCatalogOfferMessageEvent());
        this.getMessages().put(Events.GetFurniMaticBoxesPageMessageEvent, new GetFurniMaticBoxesPageEvent());
        this.getMessages().put(Events.GetFurniMaticFinalBoxMessageEvent, new GetFurniMaticFinalBoxEvent());
        this.getMessages().put(Events.GetFurniMaticRewardsPageMessageEvent, new GetFurniMaticRewardsPageEvent());
        this.getMessages().put(Events.RedeemVoucherMessageEvent, new RedeemVoucherMessageEvent());
        this.getMessages().put(Events.NewUserExperienceGiftOfferParserEvent, new NewUserExperienceGiftOfferParserEvent());
        this.getMessages().put(Events.GetSubscriptionPageMessageEvent, new GetSubscriptionPageMessageEvent());
        this.getMessages().put(Events.GetPresentsPageMessageEvent, new GetPresentsPageMessageEvent());
        this.getMessages().put(Events.GetClubPresentMessageEvent, new GetClubPresentMessageEvent());
    }

    public void registerLanding() {
        this.getMessages().put(Events.GetPromoArticlesMessageEvent, new RefreshPromoArticlesMessageEvent());
        this.getMessages().put(Events.RefreshCampaignMessageEvent, new LandingLoadWidgetMessageEvent());
    }
//
    public void registerGroups() {
        this.getMessages().put(Events.GetGroupInfoMessageEvent, new GroupInformationMessageEvent());
        this.getMessages().put(Events.GetGroupMembersMessageEvent, new GroupMembersMessageEvent());
        this.getMessages().put(Events.ManageGroupMessageEvent, new ManageGroupMessageEvent());
        this.getMessages().put(Events.RemoveGroupMemberMessageEvent, new RevokeMembershipMessageEvent());
        this.getMessages().put(Events.JoinGroupMessageEvent, new JoinGroupMessageEvent());
        this.getMessages().put(Events.UpdateGroupIdentityMessageEvent, new ModifyGroupTitleMessageEvent());
        this.getMessages().put(Events.TakeAdminRightsMessageEvent, new RevokeAdminMessageEvent());
        this.getMessages().put(Events.GiveAdminRightsMessageEvent, new GiveGroupAdminMessageEvent());
        this.getMessages().put(Events.UpdateGroupSettingsMessageEvent, new ModifyGroupSettingsMessageEvent());
        this.getMessages().put(Events.AcceptGroupMembershipMessageEvent, new AcceptMembershipMessageEvent());
        this.getMessages().put(Events.UpdateGroupBadgeMessageEvent, new ModifyGroupBadgeMessageEvent());
        this.getMessages().put(Events.SetGroupFavouriteMessageEvent, new SetFavouriteGroupMessageEvent());
        this.getMessages().put(Events.GetGroupFurniSettingsMessageEvent, new GroupFurnitureWidgetMessageEvent());
        this.getMessages().put(Events.UpdateGroupColoursMessageEvent, new GroupUpdateColoursMessageEvent());
        this.getMessages().put(Events.DeclineGroupMembershipMessageEvent, new DeclineMembershipMessageEvent());
        this.getMessages().put(Events.RemoveGroupFavouriteMessageEvent, new ClearFavouriteGroupMessageEvent());
        this.getMessages().put(Events.DeleteGroupMessageEvent, new DeleteGroupMessageEvent());
    }
//
    public void registerGroupForums() {
        this.getMessages().put(Events.GetForumStatsMessageEvent, new ForumDataMessageEvent());
        this.getMessages().put(Events.UpdateForumSettingsMessageEvent, new SaveForumSettingsMessageEvent());
        this.getMessages().put(Events.GetThreadsListDataMessageEvent, new ForumThreadsMessageEvent());
        this.getMessages().put(Events.PostGroupContentMessageEvent, new PostMessageMessageEvent());
        this.getMessages().put(Events.GetThreadDataMessageEvent, new ViewThreadMessageEvent());
        this.getMessages().put(Events.UpdateThreadMessageEvent, new UpdateThreadMessageEvent());
        this.getMessages().put(Events.GetForumsListDataMessageEvent, new GetForumsMessageEvent());
        this.getMessages().put(Events.DeleteGroupThreadMessageEvent, new HideMessageMessageEvent());
        this.getMessages().put(Events.DeleteGroupReplyMessageEvent, new HideMessageMessageEvent());
    }

    public void registerQuests() {
        this.getMessages().put(Events.GetQuestListMessageEvent, new OpenQuestsMessageEvent());
        this.getMessages().put(Events.StartQuestMessageEvent, new StartQuestMessageEvent());
        this.getMessages().put(Events.CancelQuestMessageEvent, new CancelQuestMessageEvent());
    }

    public void registerMusic() {
        this.getMessages().put(Events.SongInventoryMessageEvent, new SongInventoryMessageEvent());
        this.getMessages().put(Events.SongIdMessageEvent, new SongIdMessageEvent());
        this.getMessages().put(Events.SongDataMessageEvent, new SongDataMessageEvent());
        this.getMessages().put(Events.PlaylistAddMessageEvent, new PlaylistAddMessageEvent());
        this.getMessages().put(Events.PlaylistRemoveMessageEvent, new PlaylistRemoveMessageEvent());
        this.getMessages().put(Events.PlaylistMessageEvent, new PlaylistMessageEvent());
    }

    public void registerGameCenter() {
        this.getMessages().put(Events.GetGameAchievementsMessageEvent, new GetGameAchievementsMessageEvent());
        this.getMessages().put(Events.GetGameListMessageEvent, new GetGameListMessageEvent());
        this.getMessages().put(Events.GetGameStatusMessageEvent, new GetGameStatusMessageEvent());
        this.getMessages().put(Events.JoinGameQueueMessageEvent, new JoinGameQueueMessageEvent());
    }


    public void registerPolls() {
        this.getMessages().put(Events.GetPollMessageEvent, new GetPollMessageEvent());
        this.getMessages().put(Events.SubmitPollAnswerMessageEvent, new SubmitPollAnswerMessageEvent());
    }

    public void registerAchievements() {
        this.getMessages().put(Events.GetAchievementsMessageEvent, new AchievementsListMessageEvent());
    }

    public void handle(MessageEvent message, Session client) {
        final Short header = message.getId();

        if (Comet.isDebugging) {
            log.debug(message.toString());
        }

        if(!Comet.isRunning)
            return;

        if (this.getMessages().containsKey(header)) {
            try {
                final Event event = this.getMessages().get(header);

                if (event != null) {
                    if (this.asyncEventExecution) {
                        this.eventExecutor.submit(new MessageEventTask(event, client, message));
                    } else {
                        final long start = System.currentTimeMillis();
                        log.debug("Started packet process for packet: [" + event.getClass().getSimpleName() + "][" + header + "]");

                        event.handle(client, message);

                        long timeTakenSinceCreation = ((System.currentTimeMillis() - start));

                        // If the packet took more than 100ms to be handled, red flag!
                        if (timeTakenSinceCreation >= 100) {
                            if (client.getPlayer() != null && client.getPlayer().getData() != null)
                                log.trace("[" + event.getClass().getSimpleName() + "][" + message.getId() + "][" + client.getPlayer().getId() + "][" + client.getPlayer().getData().getUsername() + "] Packet took " + timeTakenSinceCreation + "ms to execute");
                            else
                                log.trace("[" + event.getClass().getSimpleName() + "][" + message.getId() + "] Packet took " + timeTakenSinceCreation + "ms to execute");
                        }

                        log.debug("Finished packet process for packet: [" + event.getClass().getSimpleName() + "][" + header + "] in " + ((System.currentTimeMillis() - start)) + "ms");
                    }
                }
            } catch (Exception e) {
                if (client.getLogger() != null)
                    client.getLogger().error("Error while handling event: " + this.getMessages().get(header).getClass().getSimpleName(), e);
                else
                    log.error("Error while handling event: " + this.getMessages().get(header).getClass().getSimpleName(), e);
            }
        } else if (Comet.isDebugging) {
            log.debug("Unhandled message: " + Events.valueOfId(header) + " / " + header);
        }
    }

    public Map<Short, Event> getMessages() {
        return this.messages;
    }
}
