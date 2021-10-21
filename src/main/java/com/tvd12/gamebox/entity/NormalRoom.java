package com.tvd12.gamebox.entity;

import com.tvd12.gamebox.manager.DefaultPlayerManager;
import com.tvd12.gamebox.manager.PlayerManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@SuppressWarnings({"unchecked"})
public class NormalRoom extends Room {
	
	@Setter(AccessLevel.NONE)
	protected final PlayerManager playerManager;
	
	public NormalRoom(Builder<?> builder) {
		super(builder);
		this.playerManager = builder.playerManager;
	}
	
	public void addPlayer(Player player) {
		playerManager.addPlayer(player);
	}
	
	public void removePlayer(Player player) {
		playerManager.removePlayer(player.getName());
	}
	
	public <T extends PlayerManager> T getPlayerManager() {
		return (T) playerManager;
	}
	
	@SuppressWarnings("rawtypes")
	public static Builder builder() {
		return new Builder<>();
	}
	
	public static class Builder<B extends Builder<B>> extends Room.Builder<B> {
		protected PlayerManager playerManager;
		
		public B playerManager(PlayerManager playerManager) {
			this.playerManager = playerManager;
			return (B)this;
		}
		
		public B defaultPlayerManager(int maxPlayer) {
			this.playerManager = new DefaultPlayerManager<>(maxPlayer);
			return (B)this;
		}
		
		@Override
		protected void preBuild() {
			if(playerManager == null)
				playerManager = new DefaultPlayerManager<>(999);
		}
		
		@Override
		public NormalRoom build() {
			return (NormalRoom) super.build();
		}
		
		@Override
		protected NormalRoom newProduct() {
			return new NormalRoom(this);
		}
	}
}
