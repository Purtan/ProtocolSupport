package protocolsupport.protocol.packet.middleimpl.clientbound.play.v_pe;

import org.bukkit.Material;

import protocolsupport.api.MaterialAPI;
import protocolsupport.protocol.ConnectionImpl;
import protocolsupport.protocol.packet.middle.clientbound.play.MiddleBlockAction;
import protocolsupport.protocol.packet.middleimpl.ClientBoundPacketData;
import protocolsupport.protocol.serializer.PositionSerializer;
import protocolsupport.protocol.serializer.VarNumberSerializer;
import protocolsupport.protocol.typeremapper.pe.PEPacketIDs;
import protocolsupport.utils.recyclable.RecyclableCollection;
import protocolsupport.utils.recyclable.RecyclableEmptyList;
import protocolsupport.utils.recyclable.RecyclableSingletonList;

public class BlockAction extends MiddleBlockAction {

	public BlockAction(ConnectionImpl connection) {
		super(connection);
	}

	public static final int EVENT_CHEST = 1;

	@Override
	public RecyclableCollection<ClientBoundPacketData> toData() {
		int eventType = -1;
		int eventData = 0;
		Material material = MaterialAPI.getBlockDataByNetworkTypeId(type).getMaterial();
		switch (material) {
			case SHULKER_BOX:
			case ENDER_CHEST:
			case TRAPPED_CHEST:
			case CHEST: {
				eventType = EVENT_CHEST;
				eventData = info2 > 0 ? 2 : 0; //Open or close?
				break;
			}
			default: {
				System.out.println("CATCH 22 TILEEVENT: " + material + ": " + info1 + ", " + info2);
				break;
			}
		}
		if (eventType != -1) {
			ClientBoundPacketData serializer = ClientBoundPacketData.create(PEPacketIDs.TILE_EVENT);
			PositionSerializer.writePEPosition(serializer, this.position);
			VarNumberSerializer.writeSVarInt(serializer, eventType);
			VarNumberSerializer.writeSVarInt(serializer, eventData);
			return RecyclableSingletonList.create(serializer);
		}
		return RecyclableEmptyList.get();
	}

}
