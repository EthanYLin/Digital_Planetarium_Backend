package com.sp04.digital_planetarium.websocket.service;

import com.sp04.digital_planetarium.entity.Figure;
import com.sp04.digital_planetarium.entity.Position;
import com.sp04.digital_planetarium.exception.BadRequestException;
import com.sp04.digital_planetarium.websocket.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerSocketService {
    /**
     * 新玩家连接(将玩家添加到玩家列表中)
     * @param sessionID WebSocket会话ID
     * @param token WebSocket一次性登录令牌
     * @throws BadRequestException 令牌无效
     */
    Player addPlayer(UUID sessionID, String token) throws BadRequestException;

    /**
     * 玩家离线(将玩家从玩家列表中移除)
     * @param sessionID WebSocket会话ID
     */
    void removePlayer(UUID sessionID);

    /**
     * 使用WebSocket会话ID获取玩家
     * @param sessionID WebSocket会话ID
     * @throws BadRequestException 会话ID无效
     */
    Player getPlayer(UUID sessionID) throws BadRequestException;

    /**
     * 使用用户名获取玩家
     * @param username 用户名
     */
    Optional<Player> getPlayer(String username);

    /**
     * 使用用户名获取WebSocket会话ID
     * @param username 用户名
     */
    Optional<UUID> getSessionID(String username);

    /**
     * 改变玩家的形象
     * @param sessionID WebSocket会话ID
     * @param figure 形象
     * @throws BadRequestException 会话ID无效
     */
    void changeFigure(UUID sessionID, Figure figure) throws BadRequestException;

    /**
     * 改变玩家的位置
     * @param sessionID WebSocket会话ID
     * @param position 位置
     * @throws BadRequestException 会话ID无效
     */
    void changePosition(UUID sessionID, Position position) throws BadRequestException;

    /**
     * 获取所有玩家
     * @return 玩家列表
     */
    List<Player> getAllPlayers();

    /**
     * 获取在线玩家数量
     */
    int getPlayerCount();

    boolean existPlayer(UUID sessionID);

    boolean existPlayer(String username);

}
