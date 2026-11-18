package tile;

public record TilePosition(int col, int row) {

	@Override
	public String toString() {
		return col + "," + row;
	}
}
