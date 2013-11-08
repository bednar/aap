dtoDirectory = new File(basedir, "target/generated/dto/")

assert dtoDirectory.exists()
assert dtoDirectory.listFiles().length == 0