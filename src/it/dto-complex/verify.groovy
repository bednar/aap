dtoExpected = new File(basedir, "BeerDTOExpected.txt")
assert dtoExpected.exists()

dtoDirectory = new File(basedir, "target/generated/dto/")

assert dtoDirectory.exists()
assert dtoDirectory.listFiles().length == 1

beerDTO = new File(basedir, "target/generated/dto/com/github/bednar/aap/it/dto/complex/BeerDTO.java")
beerDTO.exists()

assert dtoExpected.getText().equals(beerDTO.getText())