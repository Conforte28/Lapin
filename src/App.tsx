/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

export default function App() {
  return (
    <div className="min-h-screen bg-[#f0f2f5] text-[#1c1e21] font-bold py-10 px-4 text-[13px]">
      <div className="max-w-3xl mx-auto bg-white border border-[#dce1e8] border-t-4 border-t-[#1877f2] rounded-lg p-6 sm:p-8 shadow-md">
        <div className="border-b border-[#d8e2ef] pb-4 mb-5 flex flex-wrap items-center justify-between gap-4">
          <div className="flex items-center gap-3">
            <img
              src="/assets/aistudio/651825746073557841_95ef30e7.jpg"
              alt="Logo AZWI"
              className="w-12 h-12 object-cover border-2 border-[#1877f2] rounded-lg bg-white p-1 shadow-xs"
            />
            <div>
              <h1 className="text-xl font-black tracking-wide text-[#1877f2]" style={{ fontFamily: '"Arial Black", Arial, sans-serif' }}>
                AZWI — Version 2.0
              </h1>
              <p className="text-xs text-[#606770]">
                Suivi de production de lapins
              </p>
            </div>
          </div>
          <a
            href="/suivi-lapins.zip"
            download="suivi-lapins.zip"
            className="inline-block bg-[#1877f2] hover:bg-[#166fe5] text-white font-black text-xs px-4 py-2.5 rounded-lg shadow-sm"
          >
            Télécharger suivi-lapins.zip
          </a>
        </div>

        <p className="text-xs text-[#1c1e21]">
          Poussez vers GitHub pour redéployer sur Render sans modifier vos variables d&apos;environnement ni votre base PostgreSQL.
        </p>
      </div>
    </div>
  );
}
