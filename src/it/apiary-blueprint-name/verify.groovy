expected = """HOST: http://demoaap.apiary.io

--- Demo Application ---

"""
apiaryMd = new File(basedir, "target/generated/apiary/apiary.apib")

assert apiaryMd.exists()
assert apiaryMd.getText().equals(expected)