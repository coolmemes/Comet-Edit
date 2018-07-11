package com.cometproject.server.network.messages.incoming.group.forum.threads;

import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.groups.types.Group;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumPermission;
import com.cometproject.server.game.groups.types.components.forum.settings.ForumSettings;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThread;
import com.cometproject.server.game.groups.types.components.forum.threads.ForumThreadReply;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.group.forums.GroupForumUpdateReplyMessageComposer;
import com.cometproject.server.network.messages.outgoing.group.forums.GroupForumUpdateThreadMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.storage.queries.groups.GroupForumThreadDao;

import static com.cometproject.server.protocol.headers.Events.DeleteGroupReplyMessageEvent;

public class HideMessageMessageEvent implements Event {

    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int groupId = msg.readInt();
        int threadId = msg.readInt();
        int messageId = msg.getId() == DeleteGroupReplyMessageEvent ? msg.readInt() : -1;
        int state = msg.readInt();

        Group group = GroupManager.getInstance().get(groupId);

        if (group == null || !group.getData().hasForum()) {
            return;
        }

        ForumSettings forumSettings = group.getForumComponent().getForumSettings();

        if (forumSettings.getModeratePermission() == ForumPermission.OWNER) {
            if (client.getPlayer().getId() != group.getData().getId()) {
                return;
            }
        } else {
            if (!group.getMembershipComponent().getAdministrators().contains(client.getPlayer().getId())) {
                return;
            }
        }

        ForumThread forumThread = group.getForumComponent().getForumThreads().get(threadId);

        if (forumThread == null) {
            return;
        }

        if(!client.getPlayer().getPermissions().getRank().modTool() && state > 10) {
            state = 10;
        }

        if(messageId != -1) {
            ForumThreadReply reply = forumThread.getReplyById(messageId);

            if (reply == null) {
                return;
            }

            reply.setState(state);
            client.send(new GroupForumUpdateReplyMessageComposer(reply, threadId, groupId));
        } else {
            forumThread.setState(state);
            client.send(new GroupForumUpdateThreadMessageComposer(groupId, forumThread));
        }

        GroupForumThreadDao.saveMessageState(messageId != -1 ? messageId : threadId, state, client.getPlayer().getId(),client.getPlayer().getData().getUsername());

    }
}
