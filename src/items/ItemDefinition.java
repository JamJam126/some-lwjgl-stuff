package items;

public sealed interface ItemDefinition permits 
DropperDefinition, 
ConveyorDefinition,
FurnaceDefinition
{
	String id();
	String name();
	int price();
}
