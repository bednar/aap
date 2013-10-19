expected = """HOST: http://demoaap.apiary.io

--- Complex API IT. ---

---
This is a Complex App IT!
---

"""
apiaryMd = new File(basedir, "target/generated/apiary/Apiary.md")

assert apiaryMd.exists()
assert apiaryMd.getText().equals(expected)